package helpers

import java.util.UUID

import play.api.libs.json.{JsValue, Json}


trait Fixtures {
  val correlationId: String = UUID.randomUUID().toString

  val createRepaymentCaseJson: JsValue =
    Json.parse("""{
                  |  "sourceSystemId": "CNT",
                  |  "sourceSystemKey": "AZ1234567/7",
                  |  "sourceSystemURL" : "http://localhost:7052",
                  |  "case": {
                  |   "caseType": "Repayment",
                  |   "sourceSystemRef": "CFSRP",
                  |   "repaymentAmount": 1234567,
                  |   "taxRegime": "VAT_Trader",
                  |   "taxPeriodStart": "2016-04-06",
                  |   "taxPeriodEnd": "2017-04-05",
                  |   "claimDate": "2017-04-05",
                  |   "caseOwnerId": 666777,
                  |   "triggeredRiskRuleRef": "R123",
                  |   "taxPayer": {
                  |     "taxPayerType": "Individual",
                  |     "segment": "Plan B",
                  |     "referenceNumbers": [{
                  |       "referenceType": "ValueAddedTax",
                  |       "referenceValue": "VAT432"
                  |     }],
                  |     "nameDetails": {
                  |       "title": "Mr",
                  |       "firstName": "FirstName",
                  |       "lastName": "LastName"
                  |     },
                  |     "addresses": [
                  |       {
                  |         "correspondenceAddress": true,
                  |         "addressType": "Holiday house",
                  |         "addressLine1": "1 A Fake Road",
                  |         "addressLine2": "A Fake Place",
                  |         "city": "A Fake City",
                  |         "county": "A Fake County",
                  |         "country": "A Fake Country",
                  |         "postcode": "AB0 0CD"
                  |       }
                  |     ]
                  |   }
                  |  }
                  |}
                  |""".stripMargin)

  val createCaseRiskJson: JsValue = Json.parse(
    """{
      |  "sourceSystemId" : "CNT",
      |  "sourceSystemKey" : "VI00004-1",
      |  "sourceSystemURL" : "http://localhost:7052",
      |  "case" : {
      |    "caseType" : "YieldBearing",
      |    "sourceSystemRef" : "CFSB",
      |    "campaignId" : "CID-12345678",
      |    "projectId" : "PID-12345678",
      |    "caseId" : "123456678",
      |    "suggestedOfficerGrade" : "Major",
      |    "interventionSubType" : "Customer lead",
      |    "risks" : [
      |      {
      |        "taxRegime" : "ABSD",
      |        "subRegime" : "Returns",
      |        "firstComplianceCheck" : "Check_1",
      |        "secondComplianceCheck" : "Check_2",
      |        "inaccuracyCategory" : "Wildly",
      |        "inaccuracyDescription" : "All_over_the_place",
      |        "behaviours" : {
      |          "potentialBehaviour" : "Concerning",
      |          "emergingBehaviour" : "Devious"
      |        },
      |        "taxPeriodStart" : "2019-04-06",
      |        "taxPeriodEnd" : "2020-04-05",
      |        "riskStartDate" : "2020-04-20",
      |        "amounts" : {
      |          "potentialAmount" : 9876500,
      |          "expectedAmount" : 10008545,
      |          "emergingAmount" : 10011456
      |        },
      |        "riskScore" : 2.1
      |      }
      |    ],
      |    "taxPayer" : {
      |      "taxPayerType" : "Individual",
      |      "segment" : "Micro A",
      |      "nameDetails" : {
      |        "title" : "Professor",
      |        "firstName" : "first",
      |        "lastName" : "last"
      |      },
      |      "referenceNumbers" : [
      |        {
      |          "referenceType" : "VIRef",
      |          "referenceValue" : "VI000002343Z"
      |        }
      |      ],
      |      "addresses" : [
      |        {
      |          "correspondenceAddress" : true,
      |          "addressType" : "Registered Address",
      |          "addressLine1" : "line 1",
      |          "addressLine2" : "line 2",
      |          "city" : "city",
      |          "county" : "country",
      |          "postcode" : "ZZ11 2YY"
      |        }
      |      ]
      |    }
      |  }
      |}""".stripMargin
  )

}
