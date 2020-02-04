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

import org.scalatest.{MustMatchers, WordSpec}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.FakeRequest
import play.api.{Configuration, Environment, _}
import uk.gov.hmrc.play.bootstrap.config.{RunMode, ServicesConfig}
import config.AppConfig
import connectors.ComplianceCasesConnector
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Matchers
import org.mockito.Mockito.when
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import play.api.libs.json.Json
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}

import scala.concurrent.Future

class ComplianceCasesServiceSpec extends WordSpec with MustMatchers with GuiceOneAppPerSuite with MockitoSugar
  with ScalaFutures with IntegrationPatience {

  import scala.concurrent.ExecutionContext.Implicits.global
  import play.api.http.Status._

  private val fakeRequest = FakeRequest("GET", "/")
  private val env           = Environment.simple()
  private val configuration = Configuration.load(env)
  private val serviceConfig = new ServicesConfig(configuration, new RunMode(configuration, Mode.Dev))
  private val appConfig     = new AppConfig(configuration, serviceConfig)
  private val complianceCasesConnector = mock[ComplianceCasesConnector]
  private val service: ComplianceCasesService = new ComplianceCasesService(complianceCasesConnector)

  implicit lazy val hc: HeaderCarrier = HeaderCarrier(sessionId = None)

  val exampleJson: String =
    """{
      |     "Case": {
      |           "SourceSysRef": "CFSC",
      |           "SourceSysID": "474013587585 ",
      |           "CaseFlowID": 150000,
      |           "CampaignID": "CID-6269",
      |           "ProjectID": "PID-6480",
      |           "CaseType": "YieldBearing ",
      |           "TargetRIS": "VAT ",
      |           "ComplianceStream": "TEEL ",
      |           "EnguiryType": "Partial ",
      |           "Segment": "Medium ",
      |           "VATOfficeCode": "123456789 ",
      |           "ProjectedYield": "1230.99 ",
      |           "Filter1": "Filtering 1 ",
      |           "Filter2": "Filtering 2 ",
      |           "LastDateForEnquiry": "05SEP2019 ",
      |           "ConfidenceScore": 7.000000 ,
      |           "ArchiveApproach": "Archive ",
      |           "InterventionSubType": "Compliance Return ",
      |           "InteractionTitle": "Interaction title ",
      |           "AuthorisationType": "  ",
      |           "Risks": {
      |             "Risk": {
      |                   "TaxRegime": "VAT ",
      |                   "Description": "Example . Access the case by logging into VI and opening the link: https://hmrdevviya.ondemand.sas.com/SASVisualInvestigator/index.html#/document/Case/474013587585 ",
      |                   "Score": 9.1 ,
      |                   "TaxPeriodFrom": "2008-04-06",
      |                   "TaxPeriodTo": "2009-04-05",
      |                   "SubRegime": "VAT ",
      |                   "ComplianceCheck1": "ComplianceString1 ",
      |                   "ComplianceCheck2": "ComplianceString2 ",
      |                   "InaccuracyCategory": "Avoidance ",
      |                   "InaccuracyDescription": "Avoidance ",
      |                   "PotentialAmount": 100.00,
      |                   "ExpectedAmount": 100.00,
      |                   "PotentialBehaviour": "Mistake ",
      |                   "EmergingBehaviour": "Mistake "
      |             }
      |             },
      |       "Taxpayers": {
      |             "Taxpayer": {
      |                   "Type": "SoleTrader",
      |             "OUID": "111",
      |             "Title": "Mrs",
      |                   "FirstName": "String",
      |                   "Surname": "Tax-Payer",
      |             "NINO": "WL1234567890",
      |                   "UTR": "string",
      |                   "EmpPAYERef": "string",
      |                   "VATNumber": "string",
      |                   "Reference": "string",
      |             "TypeOfReference": "VatRegNumber",
      |                   "ValueOfReference": "Other",
      |                   "HomeAddress": {
      |                   "Line1": "2, The Tree line cul de sac",
      |                   "Line2": "string",
      |                   "Line3": "string",
      |                   "Town": "nice suburb",
      |                   "Postcode": "ns1 1pc",
      |                   "Phone": "123-123-1234"
      |                   },
      |                   "BusinessName": "The Village Pub",
      |                   "BusinessAddress": {
      |                         "Line1": "1, The High Street",
      |                   "Line2": "string",
      |                   "Line3": "string",
      |                   "Town": "Niceville",
      |                   "Postcode": "PP1 1YZ",
      |                   "Phone": "0123456"
      |                   },
      |                   "email": "tax.payer@yahoo.com"
      |             }
      |       }
      | }
      |}
      |""".stripMargin

  "Service" should {

    "return 204" in {

      when(complianceCasesConnector.complianceInvestigations(Matchers.any())(Matchers.any(),Matchers.any()))
        .thenReturn(Future.successful(HttpResponse(NO_CONTENT)))

      val response = whenReady(service.complianceInvestigations(Json.parse(exampleJson))) { response => response }

      response.status mustBe NO_CONTENT
    }
  }
}
