/*
 * Copyright 2024 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package services

import com.fasterxml.jackson.databind.JsonNode
import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.core.report.{ListReportProvider, LogLevel, ProcessingMessage, ProcessingReport}
import com.github.fge.jsonschema.main.{JsonSchema, JsonSchemaFactory}
import com.google.inject.Inject
import controllers.actions.RequestWithCorrelationId
import models.LogMessageHelper
import models.responses._
import play.api.Logger
import play.api.libs.json._

import scala.jdk.CollectionConverters._
import scala.collection.Seq

class ValidationService @Inject()(resources: ResourceService) {

  private lazy val repaymentCaseSchema = resources.getFile("/schemas/repaymentCaseType.schema.json")

  private lazy val riskCaseSchema = resources.getFile("/schemas/riskCaseType.schema.json")

  private val factory = JsonSchemaFactory
    .newBuilder()
    .setReportProvider(new ListReportProvider(LogLevel.ERROR, LogLevel.FATAL))
    .freeze()

  private val logger = Logger(this.getClass.getSimpleName)

  def validateAndRetrieveErrors(schemaString: String, input: JsValue)(
    implicit request: RequestWithCorrelationId[?]
  ): Option[JsValue] = {
    input.asOpt[JsObject] match {
      case Some(jsObject) =>
        val result: ProcessingReport = validate(schemaString, jsObject)
        val (caseTypeName, caseFieldErrors): (Option[String], Seq[FieldError]) = (input \ "case").asOpt[JsObject].map(
          validateCaseType(_)
        ).getOrElse(None -> Seq.empty[FieldError])

        if (result.isSuccess && caseFieldErrors.isEmpty) {
          None
        } else {
          caseTypeName
            .map(caseType =>
              BadRequestErrorResponse(getSequenceOfFieldErrorsFromReport(result) ++ caseFieldErrors.toSeq, caseType = caseType)
            )
            // TODO make caseType optional
            .orElse(Some(BadRequestErrorResponse(getSequenceOfFieldErrorsFromReport(result) ++ caseFieldErrors.toSeq)))
            .map(Json.toJson(_))
        }
      case _ => Some(
        Json.toJson(BadRequestErrorResponse(Seq(InvalidJsonType)))
      )
    }
  }

  private def logMessage(methodName: String, message: String)(implicit request: RequestWithCorrelationId[?]): String =
    LogMessageHelper(this.getClass.getSimpleName, methodName, message, request.correlationId).toString

  private def validateAgainstSchema(json: JsonNode, schema: JsonSchema): ProcessingReport =
    schema.validate(json, true)

  private def getFieldName(processingMessage: ProcessingMessage, prefix: String): String =
    processingMessage.asJson().get("instance").asScala.map(instanceName => prefix + instanceName.asText).headOption.getOrElse("Field cannot be found")

  private def getUnwantedOrMissingFields(fieldType: String, processingMessage: ProcessingMessage, prefix: String): List[String] = {
    Option(processingMessage.asJson().get(fieldType)).map(_.asScala.map(
      instanceName => s"${getFieldName(processingMessage, prefix)}/${instanceName.asText()}"
    ).toList).getOrElse(List())
  }

  private def getMissingFields(processingMessage: ProcessingMessage, prefix: String): List[MissingField] =
    getUnwantedOrMissingFields("missing", processingMessage, prefix) map MissingField.apply

  private def getUnexpectedFields(processingMessage: ProcessingMessage, prefix: String): List[UnexpectedField] =
    getUnwantedOrMissingFields("unwanted", processingMessage, prefix) map UnexpectedField.apply

  private def validateCaseType(caseJson: JsValue)(implicit request: RequestWithCorrelationId[?]): (Option[String], Seq[FieldError]) = {
    val methodName: String = "validateCaseType"

    def getResult(schema: String, caseType: String): (Option[String], Seq[FieldError]) = {
      val result = validate(schema, caseJson)
      if (result.isSuccess) None -> Seq.empty else {
        Some(caseType) -> getSequenceOfFieldErrorsFromReport(result, "/case")
      }
    }

    (caseJson \ "caseType").validate[String] match {
      case JsSuccess("Repayment", _) =>
        logger.info(logMessage(methodName, "Found REPAYMENT caseType attempting to validate against repayments schema"))
        getResult(repaymentCaseSchema, "Repayment")
      case JsSuccess("Risk", _) =>
        logger.info(logMessage(methodName, "Found RISK caseType attempting to validate against risk schema"))
        getResult(riskCaseSchema, "Risk")
      case JsSuccess(_, _) =>
        logger.warn(logMessage(methodName, "Found INVALID caseType in request"))
        None -> toInvalidField(JsError(__ \ "case" \ "caseType", "invalid case type provided").errors)
      case JsError(errors) =>
        logger.warn(logMessage(methodName, "caseType missing or not a string"))
        None -> toInvalidField(errors.map {
          case (_, errors) => (__ \ "case" \ "caseType", errors)
        })
    }
  }

  private def validate(schemaString: String, input: JsValue): ProcessingReport = {
    val schemaJson = JsonLoader.fromString(schemaString)
    val json = JsonLoader.fromString(Json.stringify(input))
    val schema = factory.getJsonSchema(schemaJson)
    validateAgainstSchema(json, schema)
  }

  private def getSequenceOfFieldErrorsFromReport(result: ProcessingReport, prefix: String = ""): Seq[FieldError] = {
    result.iterator.asScala.toList
      .flatMap {
        (error: ProcessingMessage) =>
          val missingAndUnexpectedFields = getMissingFields(error, prefix) ++ getUnexpectedFields(error, prefix)

          if (missingAndUnexpectedFields.isEmpty) {
            List(InvalidField(getFieldName(error, prefix)))
          } else {
            missingAndUnexpectedFields
          }
      }
  }

  private def toInvalidField(mappingErrors: Seq[(JsPath, Seq[JsonValidationError])]): Seq[InvalidField] = {
    mappingErrors.map {
      x => InvalidField(path = x._1.toString())
    }
  }
}
