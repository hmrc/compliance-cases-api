/*
 * Copyright 2020 HM Revenue & Customs
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
import play.api.mvc._

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext

class ValidationService @Inject()(val bodyParser: BodyParsers.Default, resources: ResourceService)
                                 (implicit val ec: ExecutionContext) {

  private lazy val repaymentCaseSchema = resources.getFile("/schemas/caseflowCreateRepaymentCaseSchema.json")

  private lazy val riskCaseSchema = resources.getFile("/schemas/caseflowCreateRiskCaseSchema.json")

  private val factory = JsonSchemaFactory
    .newBuilder()
    .setReportProvider(new ListReportProvider(LogLevel.ERROR, LogLevel.FATAL))
    .freeze()

  private val logger = Logger(this.getClass.getSimpleName)

  private def logMessage(methodName: String, message: String)(implicit request: RequestWithCorrelationId[_]): String = {
    LogMessageHelper(this.getClass.getSimpleName, methodName, message, request.correlationId).toString
  }

  private def validateAgainstSchema(json: JsonNode, schema: JsonSchema): ProcessingReport = {
    schema.validate(json, true)
  }

  private def getFieldName(processingMessage: ProcessingMessage, prefix: String): String = {
    processingMessage.asJson().get("instance").asScala.map(instanceName => prefix + instanceName.asText).headOption.getOrElse("Field cannot be found")
  }

  def getMissingFields(processingMessage: ProcessingMessage, prefix: String = ""): List[MissingField] = {
    Option(processingMessage.asJson().get("missing")).map(_.asScala.map(
      instanceName => MissingField(path = s"${getFieldName(processingMessage, prefix)}/${instanceName.asText()}")
    ).toList).getOrElse(List())
  }

  def getUnexpectedFields(processingMessage: ProcessingMessage, prefix: String = ""): List[UnexpectedField] = {
    Option(processingMessage.asJson().get("unwanted")).map(_.asScala.map(
      instanceName => UnexpectedField(path = s"${getFieldName(processingMessage, prefix)}/${instanceName.asText()}")
    ).toList).getOrElse(List())
  }

  def validateCaseType(caseJson: JsValue)(implicit request: RequestWithCorrelationId[_]): Either[BadRequestErrorResponse, Unit] = {
    val methodName: String = "validateCaseType"

    def getResult(schema: String, caseType: String): Either[BadRequestErrorResponse, Unit] = {
      val result = validateInternallyAgainstSchema(schema, caseJson)
      if (result.isSuccess) Right(()) else {
        Left(
          BadRequestErrorResponse(getSequenceOfFieldErrorsFromReport(result, "/case"), caseType)
        )
      }
    }

    (caseJson \ "caseType").validate[String] match {
      case JsSuccess("Repayment", _) =>
        logger.info(logMessage(methodName, "Found REPAYMENT caseType attempting to validate against repayments schema"))
        getResult(repaymentCaseSchema, "Repayment")
      case JsSuccess("YieldBearing", _) =>
        logger.info(logMessage(methodName, "Found RISK caseType attempting to validate against risk schema"))
        getResult(riskCaseSchema, "Risk")
      case JsSuccess(_, _) =>
        logger.warn(logMessage(methodName, "Found INVALID caseType in request"))
        Left(mapErrorsToBadRequestErrorResponse(JsError(__ \ "case" \ "caseType", "invalid case type provided").errors))
      case JsError(errors) =>
        logger.warn(logMessage(methodName, "caseType missing or not a string"))
        Left(mapErrorsToBadRequestErrorResponse(errors.map{
          case (_, errors) => (__ \ "case" \ "caseType", errors)
        }))
    }
  }

  private def validateInternallyAgainstSchema(schemaString: String, input: JsValue): ProcessingReport = {
    val schemaJson = JsonLoader.fromString(schemaString)
    val json = JsonLoader.fromString(Json.stringify(input))
    val schema = factory.getJsonSchema(schemaJson)
    validateAgainstSchema(json, schema)
  }

  def validate(schemaString: String, input: JsValue)(
    implicit request: RequestWithCorrelationId[_]
  ): Either[JsValue, Unit] = {
    val result = validateInternallyAgainstSchema(schemaString, input)
    if (result.isSuccess) {
      validateCaseType((input \ "case").as[JsValue])
        .fold(invalid => Left(Json.toJson(invalid)), valid => Right(valid))
    } else {
      Left(
        Json.toJson(BadRequestErrorResponse(getSequenceOfFieldErrorsFromReport(result)))
      )
    }
  }

  private def getSequenceOfFieldErrorsFromReport(result: ProcessingReport, prefix: String = ""): Seq[FieldError] = {
    result.iterator.asScala.toList
      .flatMap {
        error: ProcessingMessage =>
          val missingAndUnexpectedFields = getMissingFields(error, prefix) ++ getUnexpectedFields(error, prefix)

          if (missingAndUnexpectedFields.isEmpty) {
            List(
              InvalidField(getFieldName(error, prefix))
            )
          } else {
            missingAndUnexpectedFields
          }
      }
  }

  private def mapErrorsToBadRequestErrorResponse(mappingErrors: Seq[(JsPath, Seq[JsonValidationError])]): BadRequestErrorResponse = {
    val errors = mappingErrors.map {
      x => InvalidField(path = x._1.toString())
    }
    BadRequestErrorResponse(errors)
  }
}
