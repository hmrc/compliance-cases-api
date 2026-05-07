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

import com.fasterxml.jackson.databind.ObjectMapper
import com.networknt.schema.{Error, SchemaRegistry, SpecificationVersion}
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

  private val mapper: ObjectMapper = new ObjectMapper()
  private val schemaRegistry: SchemaRegistry =
    SchemaRegistry.withDefaultDialect(SpecificationVersion.DRAFT_4)

  private val logger = Logger(this.getClass.getSimpleName)

  def validateAndRetrieveErrors(schemaString: String, input: JsValue)(
    implicit request: RequestWithCorrelationId[?]
  ): Option[JsValue] = {
    input.asOpt[JsObject] match {
      case Some(jsObject) =>
        val schemaErrors = validate(schemaString, jsObject)
        val (caseTypeName, caseFieldErrors): (Option[String], Seq[FieldError]) = (input \ "case").asOpt[JsObject].map(
          validateCaseType(_)
        ).getOrElse(None -> Seq.empty[FieldError])

        if (schemaErrors.isEmpty && caseFieldErrors.isEmpty) {
          None
        } else {
          val combinedErrors =
            getFieldErrorsFromReport(schemaErrors) ++ caseFieldErrors

          caseTypeName
            .map(caseType =>
              BadRequestErrorResponse(combinedErrors, caseType = caseType)
            )
            // TODO make caseType optional
            .orElse(Some(BadRequestErrorResponse(combinedErrors)))
            .map(Json.toJson(_))
        }
      case _ => Some(
        Json.toJson(BadRequestErrorResponse(Seq(InvalidJsonType)))
      )
    }
  }

  private def validateCaseType(caseJson: JsValue)(
    implicit request: RequestWithCorrelationId[?]
  ): (Option[String], Seq[FieldError]) = {

    val methodName = "validateCaseType"

    def getResult(schema: String, caseType: String): (Option[String], Seq[FieldError]) = {
      val errors = validate(schema, caseJson)
      if (errors.isEmpty) {
        None -> Seq.empty
      }
      else {
        Some(caseType) -> getFieldErrorsFromReport(errors, "/case")
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

  private def validate(schemaString: String, input: JsValue): java.util.List[Error] = {
    val schemaNode = mapper.readTree(schemaString)
    val jsonNode   = mapper.readTree(Json.stringify(input))
    val schema     = schemaRegistry.getSchema(schemaNode)
    schema.validate(jsonNode)
  }

  private def getFieldErrorsFromReport(
                                        report: java.util.List[Error],
                                        prefix: String = ""
                                      ): Seq[FieldError] = {

    val extracted = report.asScala.toList.flatMap { error =>
      val missing = getMissingFields(error, prefix)
      val unexpected = getUnexpectedFields(error, prefix)

      if (unexpected.nonEmpty) {
        unexpected
      }
      else if (missing.nonEmpty) {
        missing
      }
      else {
        List(InvalidField(getFieldName(error, prefix)))
      }
    }

    extracted.sortBy {
      case _: UnexpectedField => (0, "")
      case _: MissingField    => (1, "")
      case i: InvalidField    => (2, i.path)
    }
  }

  private def getFieldName(error: Error, prefix: String): String =
    Option(error.getInstanceLocation)
      .map(loc => prefix + loc.toString)
      .getOrElse("Field cannot be found")

  private def getMissingFields(error: Error, prefix: String): List[MissingField] = {
    if (error.getKeyword == "required") {
      Option(error.getProperty)
        .map(prop => List(MissingField(s"${getFieldName(error, prefix)}/$prop")))
        .getOrElse(Nil)
    } else {
      Nil
    }
  }

  private def getUnexpectedFields(error: Error, prefix: String): List[UnexpectedField] = {
    if (error.getKeyword == "additionalProperties") {
      Option(error.getProperty)
        .map(prop => List(UnexpectedField(s"${getFieldName(error, prefix)}/$prop")))
        .getOrElse(Nil)
    } else {
      Nil
    }
  }

  private def toInvalidField(mappingErrors: Seq[(JsPath, Seq[JsonValidationError])]): Seq[InvalidField] =
    mappingErrors.map { case (path, _) => InvalidField(path.toString()) }

  private def logMessage(methodName: String, message: String)(
    implicit request: RequestWithCorrelationId[?]
  ): String =
    LogMessageHelper(
      this.getClass.getSimpleName,
      methodName,
      message,
      request.correlationId
    ).toString
}
