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

package controllers

import akka.stream.Materializer
import connectors.ComplianceCasesConnector
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}
import org.mockito.Matchers.{any, eq => meq}
import org.mockito.Mockito.when
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.http.Status
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.FakeRequest
import uk.gov.hmrc.http.HttpResponse
import data.ComplianceCasesExamples._

import scala.concurrent.Future


class ComplianceApiControllerSpec extends WordSpec with Matchers with MockitoSugar with GuiceOneAppPerSuite {

  private val connector: ComplianceCasesConnector = mock[ComplianceCasesConnector]
  override lazy val app: Application = {
    import play.api.inject._

    new GuiceApplicationBuilder()
      .overrides(
        bind[ComplianceCasesConnector].toInstance(connector)
      ).build()
  }

  implicit lazy val materializer: Materializer = app.materializer

  "The Compliance Api Controller" when {
    "serving Investigations api" should {
      "return Accepted for valid input" in {
        when(connector.complianceInvestigations(any())(any(), any()))
            .thenReturn(Future.successful(HttpResponse(ACCEPTED, Some(Json.parse(exampleJsonSuccessResponse)))))

        route(app, FakeRequest(POST, routes.ComplianceApiController.risking().url).withJsonBody(Json.parse(minimumJson))).map {
          result => {
            status(result) shouldBe Status.ACCEPTED
            contentAsJson(result) shouldBe Json.parse(exampleJsonSuccessResponse)
          }
        }
      }
      "return BadRequest for invalid input (validation error in controller)" in {
        route(app, FakeRequest(POST, routes.ComplianceApiController.risking().url).withJsonBody(Json.parse(incorrectJson))).map {
          result => status(result) shouldBe Status.BAD_REQUEST
        }
      }
      "return BadRequest for valid input (error passed back from connector)" in {
        when(connector.complianceInvestigations(any())(any(), any()))
          .thenReturn(Future.successful(HttpResponse(BAD_REQUEST, Some(Json.parse(exampleJsonErrorResponse)))))

        route(app, FakeRequest(POST, routes.ComplianceApiController.risking().url).withJsonBody(Json.parse(minimumJson))).map {
          result => {
            status(result) shouldBe Status.BAD_REQUEST
            contentAsJson(result) shouldBe Json.parse(exampleJsonErrorResponse)
          }
        }
      }
    }
  }
}
