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
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.http.Status
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.FakeRequest
import services.ComplianceCasesService
import uk.gov.hmrc.http.HttpResponse

import scala.concurrent.Future


class ComplianceApiControllerSpec extends WordSpec with Matchers with MockitoSugar with GuiceOneAppPerSuite {

  private val service: ComplianceCasesService = mock[ComplianceCasesService]
  override lazy val app: Application = {
    import play.api.inject._

    new GuiceApplicationBuilder()
      .overrides(
        bind[ComplianceCasesService].toInstance(service)
      ).build()
  }

  implicit lazy val materializer: Materializer = app.materializer

  val name = "Mr Test Name"
  val age = 20

  val minimumJson = s"""{
                       |  "Case": {
                       |    "SourceSysRef": "CFSC",
                       |    "SourceSysID": "474013587585 ",
                       |    "CaseFlowID": 150000,
                       |    "CampaignID": "CID-6269",
                       |    "ProjectID": "PID-6480",
                       |    "CaseType": "YieldBearing ",
                       |    "VATOfficeCode": "123456789 ",
                       |    "ConfidenceScore": 7.000000 ,
                       |    "Risks": {
                       |      "Risk": {
                       |        "TaxRegime": "VAT ",
                       |        "Description": "Example  ",
                       |        "Score": 9.1 ,
                       |        "TaxPeriodFrom": "2008-04-06",
                       |        "TaxPeriodTo": "2009-04-05"
                       |      }
                       |    },
                       |    "Taxpayers": {
                       |      "Taxpayer": {
                       |        "Type": "SoleTrader"
                       |      }
                       |    }
                       |  }
                       |}""".stripMargin

  val incorrectJson = s"""{
                         | "Invalid": "$name",
                         | "age": $age
                         |}""".stripMargin

  val exampleJsonResponse: String = """
                                      |{
                                      |  "Response" : "CFSC",
                                      |  "Code" : 202
                                      |}
                                      |""".stripMargin

  val exampleErrorResponse: String = """
                                       |POST of 'http://localhost:7052/compliance-cases/risking' returned 400 (Bad Request).
                                       |Response body '{"errors":["object has missing required properties ([\"Case\"])"]}'
                                       |""".stripMargin

  "The Compliance Api Controller" when {
    "serving Investigations api" should {
      "return Accepted for valid input" in {
        when(service.complianceInvestigations(any())(any(), any()))
            .thenReturn(Future.successful(HttpResponse(ACCEPTED, Some(Json.parse(exampleJsonResponse)))))

        route(app, FakeRequest(POST, routes.ComplianceApiController.risking().url).withJsonBody(Json.parse(minimumJson))).map {
          result => status(result) shouldBe Status.ACCEPTED
        }
      }
      "return BadRequest for invalid input" in {
        route(app, FakeRequest(POST, routes.ComplianceApiController.risking().url).withJsonBody(Json.parse(incorrectJson))).map {
          result => status(result) shouldBe Status.BAD_REQUEST
        }
      }
    }
  }
}
