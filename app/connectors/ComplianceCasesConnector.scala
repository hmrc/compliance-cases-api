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

package connectors

import connectors.httpParsers.ComplianceCaseConnectorParser
import javax.inject.{Inject, Singleton}
import models.LogMessageHelper
import play.api.http.{ContentTypes, HeaderNames}
import play.api.libs.json.JsValue
import play.api.{Configuration, Logger}
import uk.gov.hmrc.http.{HeaderCarrier, Authorization}
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ComplianceCasesConnector @Inject()(
  httpClient: HttpClient,
  val config: Configuration
) extends ComplianceCaseConnectorParser {

  override val className: String = this.getClass.getSimpleName
  override val logger: Logger = Logger(className)

  lazy val bearerToken: String = config.get[String]("integration-framework.auth-token")
  lazy val iFEnvironment: String = config.get[String]("integration-framework.environment")
  lazy val ifBaseUrl: String = config.get[String]("integration-framework.base-url")
  lazy val createCaseUri: String = config.get[String]("integration-framework.endpoints.create-case")

  private def headers(correlationId: String) = Seq(
    HeaderNames.CONTENT_TYPE -> ContentTypes.JSON,
    "CorrelationId" -> correlationId,
    "Environment" -> iFEnvironment,
    "Authorization" -> s"Bearer $bearerToken"
  )

  def createCase(request: JsValue, correlationId: String)
                (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[IFResponse] = {

    def logMessage(message: String): String = LogMessageHelper(className, "createCase", message, correlationId).toString

    // TODO - replace JsValue with CaseFlowCreateRequest case class
    val caseType = (request \ "case" \ "caseType").as[String]

    logger.warn(logMessage(s"request received from ComplianceCasesService and passing on the EIS through ComplianceCasesConnector with request: $request"))

    httpClient.POST[JsValue, IFResponse](s"$ifBaseUrl$createCaseUri", request, headers(correlationId))(
      implicitly, httpReads(correlationId, caseType), hc.copy(authorization = None), ec
    ).recover {
      case e: Exception =>
        logger.error(
          logMessage(
            s"Exception from when trying to talk to $ifBaseUrl$createCaseUri - ${e.getMessage} ( IF_CREATE_CASE_ENDPOINT_UNEXPECTED_EXCEPTION )"
          ), e
        )
        None
    }
  }
}
