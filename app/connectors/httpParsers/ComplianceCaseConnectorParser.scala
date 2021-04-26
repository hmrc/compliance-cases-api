/*
 * Copyright 2021 HM Revenue & Customs
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

import models.LogMessageHelper
import play.api.Logger
import play.api.http.Status._
import uk.gov.hmrc.http.{HttpReads, HttpResponse}

trait ComplianceCaseConnectorParser {
  val className: String
  type IFResponse = Option[HttpResponse]

  val logger: Logger

  def httpReads(correlationId: String): HttpReads[IFResponse] = (_, url, response) => {
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
        Some(response)
      case status if status != ACCEPTED =>
        logger.warn(
          logMessage(s"received status $status when calling $url ( IF_CREATE_CASE_ENDPOINT_UNEXPECTED_RESPONSE )")
        )
        None
      case _ =>
        logger.info(logMessage(s"received an accepted when calling $url"))
        Some(response)
    }
  }
}
