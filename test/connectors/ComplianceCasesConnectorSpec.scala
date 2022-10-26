/*
 * Copyright 2022 HM Revenue & Customs
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

import caseData.ComplianceCasesExamples._
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.http.Fault
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.http.ContentTypes
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.test.Helpers._
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext.Implicits.global

class ComplianceCasesConnectorSpec extends AnyWordSpecLike with Matchers with GuiceOneAppPerSuite
  with WireMockHelper with ScalaFutures with IntegrationPatience {


  override implicit lazy val app: Application = new GuiceApplicationBuilder()
    .configure("integration-framework.base-url" -> s"http://localhost:${server.port}", "auditing.enabled" -> false)
    .build()

  private implicit lazy val hc: HeaderCarrier = HeaderCarrier()
  val correlationId: String = "some-correlation-id"

  private def connector = app.injector.instanceOf[ComplianceCasesConnector]

  "Compliance Cases Connector" should {

    "return a HttpResponse when valid body is provided" in {
      server.stubFor(post(urlEqualTo("/organisations/case"))
        .withHeader(CONTENT_TYPE, matching(ContentTypes.JSON))
        .withHeader("CorrelationId", equalTo(correlationId))
        .withHeader("Authorization", equalTo("Bearer some-token"))
        .withHeader("Environment", equalTo("local"))
        .willReturn(
          aResponse()
            .withStatus(OK)
            .withBody(exampleJsonSuccessResponse)
            .withHeader("contentType", "application/json")
        )
      )

      whenReady(connector.createCase(Json.parse(fullCaseJson), correlationId)) { response =>
        response.get.status shouldBe OK
        response.get.json shouldBe Json.parse(exampleJsonSuccessResponse)
      }
    }

    "return an empty option if the call fails with a 500" in {
      server.stubFor(post(urlEqualTo("/organisations/case"))
        .withHeader(CONTENT_TYPE, matching(ContentTypes.JSON))
        .withHeader("CorrelationId", equalTo(correlationId))
        .withHeader("Authorization", equalTo("Bearer some-token"))
        .withHeader("Environment", equalTo("local"))
        .willReturn(serverError)
      )

      whenReady(connector.createCase(Json.parse(fullCaseJson), correlationId)) {
        _.isEmpty shouldBe true
      }
    }

    "return an empty option if the call fails with a 400" in {

      server.stubFor(post(urlEqualTo("/organisations/case"))
        .withHeader(CONTENT_TYPE, matching(ContentTypes.JSON))
        .withHeader("CorrelationId", equalTo(correlationId))
        .withHeader("Authorization", equalTo("Bearer some-token"))
        .withHeader("Environment", equalTo("local"))
        .willReturn(
          aResponse()
            .withStatus(BAD_REQUEST)
            .withBody(exampleJsonErrorResponse)
            .withHeader("contentType", "application/json")
        )
      )

      whenReady(connector.createCase(Json.parse(fullCaseJson), correlationId)) {
        _.isEmpty shouldBe true
      }
    }

    "return an empty option if the call fails with a 404" in {

      server.stubFor(post(urlEqualTo("/organisations/case"))
        .withHeader(CONTENT_TYPE, matching(ContentTypes.JSON))
        .withHeader("CorrelationId", equalTo(correlationId))
        .withHeader("Authorization", equalTo("Bearer some-token"))
        .withHeader("Environment", equalTo("local"))
        .willReturn(notFound())
      )

      whenReady(connector.createCase(Json.parse(fullCaseJson), correlationId)) {
        _.isEmpty shouldBe true
      }
    }

    "return an empty option if the call fails with a 401" in {

      server.stubFor(post(urlEqualTo("/organisations/case"))
        .withHeader(CONTENT_TYPE, matching(ContentTypes.JSON))
        .withHeader("CorrelationId", equalTo(correlationId))
        .withHeader("Authorization", equalTo("Bearer some-token"))
        .withHeader("Environment", equalTo("local"))
        .willReturn(unauthorized())
      )

      whenReady(connector.createCase(Json.parse(fullCaseJson), correlationId)) {
        _.isEmpty shouldBe true
      }
    }

    "return an empty option if the call fails with an exception" in {

      def exception = aResponse.withFault(Fault.CONNECTION_RESET_BY_PEER)

      server.stubFor(post(urlEqualTo("/organisations/case"))
        .withHeader(CONTENT_TYPE, matching(ContentTypes.JSON))
        .withHeader("CorrelationId", equalTo(correlationId))
        .withHeader("Authorization", equalTo("Bearer some-token"))
        .withHeader("Environment", equalTo("local"))
        .willReturn(exception)
      )

      whenReady(connector.createCase(Json.parse(fullCaseJson), correlationId)) {
        _.isEmpty shouldBe true
      }
    }
  }
}
