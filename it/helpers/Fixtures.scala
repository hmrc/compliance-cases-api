package helpers

import java.util.UUID

import play.api.libs.json.{JsValue, Json}


trait Fixtures {
  val correlationId: String = UUID.randomUUID().toString

  val createRepaymentCaseJson: JsValue =
    Json.parse("""{
      |  "sourceSystemId": "CNT",
      |  "sourceSystemKey": "AZ1234567/7",
      |  "case": {
      |   "caseType": "Repayment",
      |   "sourceSystemRef": "CFSRP",
      |   "repaymentAmount": 1234567.99,
      |   "taxRegime": "VAT_Trader",
      |   "taxPeriodStart": "2016-04-06",
      |   "taxPeriodEnd": "2017-04-05",
      |   "caseOwnerId": 666777,
      |   "triggeredRiskRuleRef": "Abc$123",
      |   "taxPayer": {
      |     "taxPayerType": "Individual",
      |     "segment": "Plan B",
      |     "referenceNumber": {
      |       "referenceType": "ValueAddedTax",
      |       "referenceValue": "VAT-666"
      |     },
      |     "nameDetails": {
      |       "title": "MR",
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

}
