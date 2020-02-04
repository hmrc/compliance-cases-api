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

package connectors

import play.api.Logger
import uk.gov.hmrc.http.{BadRequestException, HeaderCarrier, HttpResponse, NotFoundException, Upstream4xxResponse, Upstream5xxResponse}

trait RecoveryUtil {
  def recovery(implicit hc: HeaderCarrier, api: String): PartialFunction[Throwable, HttpResponse] = {

    case badRequest: BadRequestException =>
      Logger.error(s"Bad Request response from $api API - ${badRequest.message}")
      HttpResponse(badRequest.responseCode, responseString = Some(badRequest.message))

    case notFoundException: NotFoundException =>
      Logger.error(s"Not Found response from $api API - ${notFoundException.message}")
      HttpResponse(notFoundException.responseCode, responseString = Some(notFoundException.message))

    case upstream4xxResponse: Upstream4xxResponse =>
      Logger.error(s"4XX response from $api API - ${upstream4xxResponse.message}")
      HttpResponse(upstream4xxResponse.upstreamResponseCode, responseString = Some(upstream4xxResponse.message))

    case upstream5xxResponse: Upstream5xxResponse =>
      Logger.error(s"5XX response from $api API - ${upstream5xxResponse.message}")
      HttpResponse(upstream5xxResponse.upstreamResponseCode, responseString = Some(upstream5xxResponse.message))

    case e: Exception =>
      Logger.error(s"Exception from $api API - ${e.getMessage}")
      HttpResponse(500, responseString = Some(e.getMessage))
  }
}
