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

package helpers

import play.api.libs.json.{JsValue, Json}

import java.util.UUID


trait Fixtures {
  val correlationId: String = UUID.randomUUID().toString

  val createRepaymentCaseJson: JsValue =
    Json.parse("""{
                 |  "sourceSystemId": "CNT",
                 |  "sourceSystemKey": "VI00004-1",
                 |  "sourceSystemURL": "http://localhost:7052",
                 |  "taxPayer": {
                 |    "taxPayerType": "Organisation",
                 |    "segment": "Micro A",
                 |    "nameDetails": {
                 |      "organisationName": "Bodgit & Scarper"
                 |    },
                 |    "referenceNumbers": [
                 |      {
                 |        "referenceType": "VIRef",
                 |        "referenceValue": "VI000002343B"
                 |      }
                 |    ],
                 |    "addresses": [
                 |      {
                 |        "correspondenceAddress": true,
                 |        "addressType": "Registered Address",
                 |        "addressLine1": "Megabucks House",
                 |        "addressLine2": "Richville Place",
                 |        "city": "Richville",
                 |        "county": "Wadshire",
                 |        "postcode": "ZZ11 2YY"
                 |      },
                 |      {
                 |        "correspondenceAddress": false,
                 |        "addressType": "Main Office",
                 |        "addressLine1": "The Hovel",
                 |        "addressLine2": "Downtrodden Way",
                 |        "city": "Poorsville",
                 |        "county": "Wadshire",
                 |        "country": "UK",
                 |        "postcode": "ZZ11 2YY"
                 |      }
                 |    ]
                 |  },
                 |  "case": {
                 |    "caseType": "Repayment",
                 |    "sourceSystemRef": "CFSRP",
                 |    "campaignId": "CID-00002344",
                 |    "projectId": "PID-98765432",
                 |    "repaymentAmount": 123456789012,
                 |    "taxRegime": "VATC",
                 |    "taxPeriodStart": "2019-04-06",
                 |    "taxPeriodEnd": "2020-04-05",
                 |    "caseOwnerId": 666,
                 |    "triggeredRiskRuleRef": "R0123",
                 |    "oudn": "Bolton-2437-C",
                 |    "claimDate": "2020-04-17"
                 |  }
                 |}
                  |""".stripMargin)

  val createCaseRiskJson: JsValue = Json.parse(
    """{
      |  "sourceSystemId": "CNT",
      |  "sourceSystemKey": "VI00004-1",
      |  "sourceSystemURL": "http://localhost:7052",
      |  "taxPayer": {
      |    "taxPayerType": "Individual",
      |    "segment": "Micro A",
      |    "nameDetails": {
      |      "title": "Dr",
      |      "firstName": "Donald also known as 'A bloke with a very long first name like Robert'",
      |      "lastName": "Duck"
      |    },
      |    "referenceNumbers": [
      |      {
      |        "referenceType": "VIRef",
      |        "referenceValue": "VI000002343Z"
      |      }
      |    ],
      |    "addresses": [
      |      {
      |        "correspondenceAddress": true,
      |        "addressType": "Registered Address",
      |        "addressLine1": "Megabucks House",
      |        "addressLine2": "Richville Place",
      |        "city": "Richville",
      |        "county": "Wadshire",
      |        "postcode": "ZZ11 2YY"
      |      },
      |      {
      |        "correspondenceAddress": false,
      |        "addressType": "Main Office",
      |        "addressLine1": "The Hovel",
      |        "addressLine2": "Downtrodden Way",
      |        "city": "Poorsville",
      |        "county": "Wadshire",
      |        "country": "UK",
      |        "postcode": "ZZ11 2YY"
      |      }
      |    ]
      |  },
      |  "case": {
      |    "caseType": "Risk",
      |    "sourceSystemRef": "CFSB",
      |    "campaignId": "CID-00002344",
      |    "projectId": "PID-98765432",
      |    "targetRIS": "AB_C",
      |    "complianceStream": "Fraud",
      |    "enquiryType": "AB/00123-C",
      |    "vatOfficeCode": "VAT-0063476P",
      |    "lastDateForEnquiry": "2021-01-30",
      |    "confidenceScore": 1.3,
      |    "archiveApproach": "Extended",
      |    "interactionTitle": "Slow and careful",
      |    "authorisationType": "Dictatorial",
      |    "suggestedOfficerGrade": "Major",
      |    "interventionSubType": "Customer lead",
      |    "risks": [
      |      {
      |        "taxRegime": "VATC",
      |        "riskDescription": "Something dodgy",
      |        "subRegime": "Returns",
      |        "firstComplianceCheck": "Check_1",
      |        "secondComplianceCheck": "Check_2",
      |        "inaccuracyCategory": "Wildly",
      |        "inaccuracyDescription": "All_over_the_place",
      |        "behaviours": {
      |          "potentialBehaviour": "Concerning",
      |          "emergingBehaviour": "Devious"
      |        },
      |        "taxPeriodStart": "2019-04-06",
      |        "taxPeriodEnd": "2020-04-05",
      |        "riskStartDate": "2020-04-20",
      |        "amounts": {
      |          "potentialAmount": -999999999999,
      |          "expectedAmount": 999999999999,
      |          "emergingAmount": 10011456
      |        },
      |        "riskScore": 2.1
      |      }
      |    ]
      |  }
      |}""".stripMargin
  )

}
