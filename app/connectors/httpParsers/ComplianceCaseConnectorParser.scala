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

package connectors.httpParsers

import models.{Error, ErrorResponse, LogMessageHelper}
import play.api.Configuration
import play.api.Logger
import play.api.http.Status._
import play.api.libs.json._
import uk.gov.hmrc.http.HttpResponse

trait ComplianceCaseConnectorParser {

  val className: String
  type IFResponse = Option[HttpResponse]

  val config: Configuration
  lazy val errorResponseMap: Map[String, String] = config.get[Map[String, String]]("errorMessages")

  val logger: Logger = Logger(getClass)

  //      TODO: Split out calls
  def customHttpRead(correlationId: String, caseType: String)(url: String, response: HttpResponse): IFResponse = {
    def logMessage(message: String): String = LogMessageHelper(className, "createCase", message, correlationId).toString
    response.status match {
      case NOT_FOUND =>
        logger.warn(
          logMessage(s"received a not found status when calling $url ( IF_CREATE_CASE_ENDPOINT_NOT_FOUND_RESPONSE )")
        )
        None
      case BAD_REQUEST =>
        logger.warn(
          logMessage(s"received a bad request status when calling $url with body: ${response.body} ( IF_CREATE_CASE_ENDPOINT_BAD_REQUEST_RESPONSE )")
        )
        None
      case UNPROCESSABLE_ENTITY =>
        logger.warn(
          logMessage(s"received an unprocessable entity status when calling $url with body: ${response.body}")
        )
        Some(httpErrorResponse(response, caseType))
      case status if status != OK =>
        logger.warn(
          logMessage(s"received status $status when calling $url ( IF_CREATE_CASE_ENDPOINT_UNEXPECTED_RESPONSE )")
        )
        None
      case _ =>
        logger.info(logMessage(s"received an OK when calling $url"))
        Some(response)
    }
  }

  def httpErrorResponse(response: HttpResponse, caseType: String): HttpResponse = {
    HttpResponse(
      UNPROCESSABLE_ENTITY,
      Json.toJson(errorResponse(response, caseType)).toString,
      response.headers
    )
  }

  def errorResponse(response: HttpResponse, caseType: String): ErrorResponse =
    ErrorResponse(caseType, failures(response, caseType))

  def failures(response: HttpResponse, caseType: String): List[Error] =
    (response.json \ "failures").as[JsArray].value.map{ failure =>
      error((failure \ "code").as[String], caseType)
    }.toList

  def error(code: String, caseType: String): Error = {
    Error(
      code,
      errorResponseMap.get(s"$caseType:$code").fold(throw new RuntimeException("missing configuration message"))(identity)
    )
  }

}
