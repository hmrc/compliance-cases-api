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

import com.github.tomakehurst.wiremock.client.WireMock._
import org.scalatest.{MustMatchers, WordSpec}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalatest.concurrent.ScalaFutures
import play.api.http.ContentTypes
import play.api.libs.json.Json
import play.api.test.Helpers._
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import data.ComplianceCasesExamples._
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder

import scala.concurrent.duration.Duration

class ComplianceCasesConnectorSpec extends WordSpec with MustMatchers with GuiceOneAppPerSuite
  with WireMockHelper with ScalaFutures {

  override implicit lazy val app: Application = new GuiceApplicationBuilder()
    .configure("microservice.services.compliance-cases.port" -> server.port)
    .build()

  private implicit lazy val hc: HeaderCarrier = HeaderCarrier()
  private lazy val connector = app.injector.instanceOf[ComplianceCasesConnector]

  "Compliance Cases Connector" should {

    "return a HttpResponse when valid body is provided" in {

        server.stubFor(post(urlEqualTo("/compliance-cases/risking"))
        .withHeader(CONTENT_TYPE, matching(ContentTypes.JSON))
        .willReturn(
          aResponse()
            .withStatus(ACCEPTED)
            .withBody(exampleJsonSuccessResponse)
            .withHeader("contentType", "application/json")
        )
      )

      val response = Await.result(connector.complianceInvestigations(Json.parse(fullCaseJson)), Duration.Inf)
      response.status mustBe ACCEPTED
      response.json mustBe Json.parse(exampleJsonSuccessResponse)
    }

    "return an 500 when call fails" in {

      server.stubFor(post(urlEqualTo("/compliance-cases/risking"))
        .withHeader(CONTENT_TYPE, matching(ContentTypes.JSON))
        .willReturn(serverError)
      )

      val response = Await.result(connector.complianceInvestigations(Json.parse(fullCaseJson)), Duration.Inf)
      response.status mustBe INTERNAL_SERVER_ERROR
    }

    "return an 400 when call fails" in {

      server.stubFor(post(urlEqualTo("/compliance-cases/risking"))
        .withHeader(CONTENT_TYPE, matching(ContentTypes.JSON))
        .willReturn(
          aResponse()
            .withStatus(BAD_REQUEST)
            .withBody(exampleJsonErrorResponse)
            .withHeader("contentType", "application/json")
        )
      )

      val response = Await.result(connector.complianceInvestigations(Json.parse(fullCaseJson)), Duration.Inf)
      response.status mustBe BAD_REQUEST
    }

    "return an 404 when call fails" in {

      server.stubFor(post(urlEqualTo("/compliance-cases/risking"))
        .withHeader(CONTENT_TYPE, matching(ContentTypes.JSON))
        .willReturn(notFound())
      )

      val response = Await.result(connector.complianceInvestigations(Json.parse(fullCaseJson)), Duration.Inf)
      response.status mustBe NOT_FOUND
    }

    "return an 401 when call fails" in {

      server.stubFor(post(urlEqualTo("/compliance-cases/risking"))
        .withHeader(CONTENT_TYPE, matching(ContentTypes.JSON))
        .willReturn(unauthorized())
      )

      val response = Await.result(connector.complianceInvestigations(Json.parse(fullCaseJson)), Duration.Inf)
      response.status mustBe UNAUTHORIZED
    }

  }
}
