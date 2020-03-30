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
import models.responses.{BadRequestErrorResponse, InvalidField, MissingField}
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
  private val logger = Logger(this.getClass)

  def validateAgainstSchema(json: JsonNode, schema: JsonSchema): ProcessingReport = {
    schema.validate(json, true)
  }

  def getFieldName(processingMessage: ProcessingMessage, prefix: String = "") = {
    processingMessage.asJson().get("instance").asScala.map(instanceName => prefix + instanceName.asText).headOption.getOrElse("Field cannot be found")
  }

  def getMissingFields(processingMessage: ProcessingMessage, prefix: String = ""): List[MissingField] = {
    Option(processingMessage.asJson().get("missing")).map(_.asScala.map(
      instanceName => MissingField(path = s"${getFieldName(processingMessage, prefix)}/${instanceName.asText()}")
    ).toList).getOrElse(List())
  }

  def validateCaseType(caseJson: JsValue): Either[BadRequestErrorResponse, Unit] = {
    def getResult(schema: String, caseType: String): Either[BadRequestErrorResponse, Unit] = {
      val result = validateInternallyAgainstSchema(schema, caseJson)
      if (result.isSuccess) Right(()) else {
        val errors = getJsonObjs(result, "/case")
        errors.foreach(g => logger.error(g.toString()))
        Left(
          BadRequestErrorResponse(errors, caseType)
        )
      }
    }

    (caseJson \ "caseType").validate[String] match {
      case JsSuccess("Repayment", _) => getResult(repaymentCaseSchema, "Repayment")
      case JsSuccess("Risk", _) => getResult(riskCaseSchema, "Risk")
      case JsSuccess(_, _) => Left(mappingErrorResponse(JsError(__ \ "case" \ "caseType", "invalid case type provided").errors))
      case JsError(errors) => Left(mappingErrorResponse(errors))
    }
  }

  private def validateInternallyAgainstSchema(schemaString: String, input: JsValue) = {
    val schemaJson = JsonLoader.fromString(schemaString)
    val json = JsonLoader.fromString(Json.stringify(input))
    val schema = factory.getJsonSchema(schemaJson)
    validateAgainstSchema(json, schema)
  }

  def validate[A](schemaString: String, input: JsValue)(implicit rds: Reads[A]): Either[JsValue, Unit] = {
    val result = validateInternallyAgainstSchema(schemaString, input)
    if (result.isSuccess) {
      validateCaseType((input \ "case").as[JsValue]).flatMap(_ =>
        Json.fromJson[A](input) match {
          case JsSuccess(value, path) => Right(())
          case JsError(errors) => Left(mappingErrorResponse(errors))
        }
      ).fold(invalid => Left(Json.toJson(invalid)), valid => Right(valid))
    } else {

      //Uncomment if want to log request json
      //logger.debug(Json.prettyPrint(input))
      val errors = getJsonObjs(result)
      errors.foreach(g => logger.error(g.toString()))
      Left(
        Json.toJson(BadRequestErrorResponse(errors))
      )
    }
  }

  def getJsonObjs(result: ProcessingReport, prefix: String = "") = {
    result.iterator.asScala.toList
      .flatMap {
        error =>
          val missingFields = getMissingFields(error, prefix)
          if (missingFields.isEmpty) {
            List(
              InvalidField(getFieldName(error, prefix))
            )
          } else {
            missingFields
          }
      }
  }

  private def mappingErrorResponse(mappingErrors: Seq[(JsPath, Seq[JsonValidationError])]): BadRequestErrorResponse = {
    val errors = mappingErrors.map {
      x => InvalidField(path = x._1.toString())
    }
    BadRequestErrorResponse(errors)
  }
}
