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

import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.core.report.{ListReportProvider, LogLevel}
import com.github.fge.jsonschema.main.JsonSchemaFactory
import com.google.inject.Inject
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Results._
import play.api.mvc._

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}

class ValidationService @Inject()(val bodyParser: BodyParsers.Default)
                                 (implicit val ec: ExecutionContext) {

  private val factory = JsonSchemaFactory
    .newBuilder()
    .setReportProvider(new ListReportProvider(LogLevel.ERROR, LogLevel.FATAL))
    .freeze()
  private val logger = Logger(this.getClass)

  def validate(schemaString: String, jsValue: JsValue): Either[JsValue, Unit] = {

    val schemaJson = JsonLoader.fromString(schemaString)
    val json = JsonLoader.fromString(Json.stringify(jsValue))
    val schema = factory.getJsonSchema(schemaJson)
    val result = schema.validate(json, true)

    if (result.isSuccess) {
      Right(())
    } else {

      val errors = result.iterator.asScala.toList.map {
        _.getMessage
      }

      logger.error(Json.prettyPrint(jsValue))
      errors.foreach(logger.error(_))

      Left(Json.obj(
        "errors" -> errors
      ))
    }
  }

  def apply(schemaString: String): ActionFilter[Request] with ActionBuilder[Request, AnyContent] =
    new ActionFilter[Request] with ActionBuilder[Request, AnyContent] {
      override protected def filter[A](request: Request[A]): Future[Option[Result]] =
        validate(schemaString, request.body.asInstanceOf[JsValue]) match {
          case Right(_) => Future.successful(None)
          case Left(errors) => Future.successful(Some(BadRequest(errors)))
        }

      override def parser: BodyParser[AnyContent] = bodyParser

      override protected def executionContext: ExecutionContext = ec
    }
}
