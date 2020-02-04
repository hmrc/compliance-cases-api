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

package models

import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json

class ComplianceInvestigationsModelSpec extends WordSpec with Matchers {

  val caseRef = "CSFC-1234567890"
  val name = "Mr Test Name"
  val age = 20

  "Compliance Investigations model" when {
    val filledModel =
      ComplianceInvestigationsModel(
        CaseflowCaseModel(
          "CFSC", "474013587585 ", 150000, "CID-6269", "PID-6480", "YieldBearing ", None, None, None, None,
          "123456789 ", None, None, None, None, 7.00, None, None, None, None,
          RisksModel(RiskModel("VAT ", "Example  ", 9.1 , "2008-04-06", "2009-04-05", None, None, None, None, None, None, None, None, None)),
          TaxpayersModel(TaxpayerModel("SoleTrader", None, None, None, None, None, None, None, None, None, None, None, None, None, None, None))
        )
      )
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

    val incorrectJson =
      s"""{
        | "Invalid": "$name",
        | "age": $age
        |}""".stripMargin

    "binding json data to a model" should {
      "produce a full model" in {
        Json.fromJson[ComplianceInvestigationsModel](Json.parse(minimumJson)).getOrElse(None) shouldBe filledModel
      }
      "return Errors if missing data" in {
        Json.fromJson[ComplianceInvestigationsModel](Json.parse(incorrectJson)).isError shouldBe true
      }
    }
  }

}
