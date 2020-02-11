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
import com.github.fge.jsonschema.core.report.{ListReportProvider, LogLevel, ProcessingReport}
import com.github.fge.jsonschema.main.{JsonSchema, JsonSchemaFactory}
import com.google.inject.Inject
import models.ComplianceInvestigations
import play.api.Logger
import play.api.libs.json._
import play.api.mvc._

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext

class ValidationService @Inject()(val bodyParser: BodyParsers.Default)
                                 (implicit val ec: ExecutionContext) {

  private val factory = JsonSchemaFactory
    .newBuilder()
    .setReportProvider(new ListReportProvider(LogLevel.ERROR, LogLevel.FATAL))
    .freeze()
  private val logger = Logger(this.getClass)

  def validateAgainstSchema(json: JsonNode, schema: JsonSchema): ProcessingReport = {
    schema.validate(json, true)
  }

  def validate[A](schemaString: String, input: JsValue)(implicit rds: Reads[A]): Either[JsValue, Unit] = {

    val schemaJson = JsonLoader.fromString(schemaString)
    val json = JsonLoader.fromString(Json.stringify(input))
    val schema = factory.getJsonSchema(schemaJson)
    val result = validateAgainstSchema(json, schema)

    if (result.isSuccess) {
      Json.fromJson[A](input) match {
        case JsSuccess(value, path) => Right()
        case JsError(errors) => Left(mappingErrorResponse(errors))
      }
    } else {

      val errors = result.iterator.asScala.toList.map(_.getMessage)

      //Uncomment if want to log request json
      //logger.debug(Json.prettyPrint(input))
      errors.foreach(logger.error(_))

      Left(Json.obj("errors" -> errors))
    }
  }

  private def mappingErrorResponse(mappingErrors: Seq[(JsPath, Seq[JsonValidationError])]): JsValue = {

    val errors = mappingErrors.map(x => s"${x._1.toString()} - ${x._2.map(_.message).mkString(",")}")

    Json.obj("mappingErrors" -> errors)
  }
}
