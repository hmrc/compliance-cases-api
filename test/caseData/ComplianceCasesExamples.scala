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

package caseData

import models.{Address, ComplianceInvestigations, HumanNameDetails, NameDetails, OrganisationNameDetails, ReferenceNumber, RepaymentCase, RiskCase, TaxPayer}


object ComplianceCasesExamples {

  val exampleJsonSuccessResponse: String =
    """
      |{
      |  "Response" : "CFSC",
      |  "Code" : 202
      |}
      |""".stripMargin

  val exampleJsonErrorResponse: String = """{"errors":["object has missing required properties ([\"Case\"])"]}""".stripMargin

  val id = 150000

  val filledMinimumRepaymentOrganisationModel: ComplianceInvestigations = ComplianceInvestigations(
    sourceSystemId = "CNT",
    sourceSystemKey = "AZ1234567/7",
    `case` = RepaymentCase(
      caseType = "Repayment",
      sourceSystemRef = "CFSRP",
      repaymentAmount = 1234567.99,
      taxRegime = "VAT_Trader",
      taxPeriodStart = "2016-04-06",
      taxPeriodEnd = "2017-04-05",
      caseOwnerId = None,
      triggeredRiskRuleRef = None,
      taxPayer = TaxPayer(
        taxPayerType = "Organisation",
        segment = None,
        referenceNumber = ReferenceNumber("ValueAddedTax", "VAT-666"),
        nameDetails = Some(OrganisationNameDetails("Smiths Store")),
        addresses = Seq(
          Address(correspondenceAddress = true, "Holiday house", "1 Makebelieve Avenue", None, None, None, None, None)
        )
      )
    )
  )

  val filledMinimumRepaymentHumanModel: ComplianceInvestigations = ComplianceInvestigations(
    sourceSystemId = "CNT",
    sourceSystemKey = "AZ1234567/7",
    `case` = RepaymentCase(
      caseType = "Repayment",
      sourceSystemRef = "CFSRP",
      repaymentAmount = 1234567.99,
      taxRegime = "VAT_Trader",
      taxPeriodStart = "2016-04-06",
      taxPeriodEnd = "2017-04-05",
      caseOwnerId = None,
      triggeredRiskRuleRef = None,
      taxPayer = TaxPayer(
        taxPayerType = "Individual",
        segment = None,
        referenceNumber = ReferenceNumber("ValueAddedTax", "VAT-666"),
        nameDetails = Some(HumanNameDetails("MR", "John", "Smith")),
        addresses = Seq(
          Address(correspondenceAddress = true, "Holiday house", "1 Makebelieve Avenue", None, None, None, None, None)
        )
      )
    )
  )

  val filledMinimumRiskModel: ComplianceInvestigations = ComplianceInvestigations(
    sourceSystemId = "CNT",
    sourceSystemKey = "AZ1234567/7",
    `case` = RiskCase(
      caseType = "Risk",
      sourceSystemRef = "CFSRK"
    )
  )

  val minimumRepaymentHumanJson: String =
    """{
      | "sourceSystemId": "CNT",
      | "sourceSystemKey": "AZ1234567/7",
      | "case": {
      |   "caseType": "Repayment",
      |   "sourceSystemRef": "CFSRP",
      |   "repaymentAmount": 1234567.99,
      |   "taxRegime": "VAT_Trader",
      |   "taxPeriodStart": "2016-04-06",
      |   "taxPeriodEnd": "2017-04-05",
      |   "taxPayer": {
      |     "taxPayerType": "Individual",
      |     "nameDetails": {
      |       "title": "MR",
      |       "firstName": "John",
      |       "lastName": "Smith"
      |     },
      |     "referenceNumber": {
      |       "referenceType": "ValueAddedTax",
      |       "referenceValue": "VAT-666"
      |     },
      |     "addresses": [
      |       {
      |         "correspondenceAddress": true,
      |         "addressType": "Holiday house",
      |         "addressLine1": "1 Makebelieve Avenue"
      |       }
      |     ]
      |   }
      | }
      |}""".stripMargin

  val minimumRiskJson: String =
    """{
      | "sourceSystemId": "CNT",
      | "sourceSystemKey": "AZ1234567/7",
      | "case": {
      |   "caseType": "Risk",
      |   "sourceSystemRef": "CFSRK"
      | }
      |}""".stripMargin

  val minimumRepaymentOrganisationJson: String =
    """{
      | "sourceSystemId": "CNT",
      | "sourceSystemKey": "AZ1234567/7",
      | "case": {
      |   "caseType": "Repayment",
      |   "sourceSystemRef": "CFSRP",
      |   "repaymentAmount": 1234567.99,
      |   "taxRegime": "VAT_Trader",
      |   "taxPeriodStart": "2016-04-06",
      |   "taxPeriodEnd": "2017-04-05",
      |   "taxPayer": {
      |     "taxPayerType": "Organisation",
      |     "nameDetails": {
      |       "organisationName": "Smiths Store"
      |     },
      |     "referenceNumber": {
      |       "referenceType": "ValueAddedTax",
      |       "referenceValue": "VAT-666"
      |     },
      |     "addresses": [
      |       {
      |         "correspondenceAddress": true,
      |         "addressType": "Holiday house",
      |         "addressLine1": "1 Makebelieve Avenue"
      |       }
      |     ]
      |   }
      | }
      |}""".stripMargin

  val addressJson: String =
    s"""{
       | "sourceSystemId": "CNT",
       | "sourceSystemKey": "AZ1234567/7",
       | "case": {
       |   "caseType": "Repayment",
       |   "sourceSystemRef": "CFSRP",
       |   "repaymentAmount": 1234567.99,
       |   "taxRegime": "VAT_Trader",
       |   "taxPeriodStart": "2016-04-06",
       |   "taxPeriodEnd": "2017-04-05",
       |   "taxPayer": {
       |     "taxPayerType": "Organisation",
       |     "referenceNumber": {
       |       "referenceType": "ValueAddedTax",
       |       "referenceValue": "VAT-666"
       |     },
       |     "addresses": [
       |       {
       |         "correspondenceAddress": true,
       |         "addressType": "Home",
       |         "addressLine1": "1 Downtoearth Street",
       |         "addressLine2": "Mundanesville",
       |         "city": "Bored City",
       |         "county": "Sleepy State",
       |         "country": "US of A",
       |         "postcode": "CA17745"
       |       }
       |     ]
       |   }
       | }
       |}
       |""".stripMargin

  val incorrectJson: String =
    """{
      | "Invalid": "Invalid",
      | "age": 54
      |}
      |""".stripMargin

  val fullCaseJson: String =
    """{
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
      |       "firstName": "John",
      |       "lastName": "Smith"
      |     },
      |     "addresses": [
      |       {
      |         "correspondenceAddress": true,
      |         "addressType": "Holiday house",
      |         "addressLine1": "1 Makebelieve Avenue",
      |         "addressLine2": "Johns Place",
      |         "city": "A Real City",
      |         "county": "Johns County",
      |         "country": "Johns Country",
      |         "postcode": "QW1 1QW"
      |       }
      |     ]
      |   }
      |  }
      |}
      |""".stripMargin

  val caseflowCreateCaseSchema: String =
    """|{
       |  "$schema": "http://json-schema.org/draft-04/schema#",
       |  "title": "API#1561 -  Create Case Request Schema v0.1.0",
       |  "type": "object",
       |  "additionalProperties": false,
       |  "required": [
       |    "sourceSystemId",
       |    "sourceSystemKey",
       |    "case"
       |  ],
       |  "properties": {
       |    "sourceSystemId": {
       |      "description": "To identify the source system. Currently only 'CNT' (Connect) supported",
       |      "type": "string",
       |      "enum": [
       |        "CNT"
       |      ]
       |    },
       |    "sourceSystemKey": {
       |      "description": "Primary key on the source system",
       |      "type": "string",
       |      "pattern": "^[A-Za-z0-9\/\\$\\.\\-]{1,64}$"
       |    },
       |    "case": {
       |      "type": "object"
       |    }
       |  },
       |  "definitions": {
       |  }
       |}
       |""".stripMargin

  val caseflowCreateRepaymentCaseSchema: String =
    """|{
       |  "$schema": "http://json-schema.org/draft-04/schema#",
       |  "title": "API#1561 -  Create Case Request Schema v0.1.0",
       |  "type": "object",
       |  "additionalProperties": false,
       |  "required": [
       |    "caseType",
       |    "sourceSystemRef",
       |    "repaymentAmount",
       |    "taxRegime",
       |    "taxPeriodStart",
       |    "taxPeriodEnd",
       |    "taxPayer"
       |  ],
       |  "properties": {
       |    "caseType": {
       |      "description": "Mandatory. Must be 'Repayment' for a   repayment case",
       |      "type": "string",
       |      "enum": [
       |        "Repayment"
       |      ]
       |    },
       |    "sourceSystemRef": {
       |      "description": "Mandatory. An identify reference; 'CFSRP' for a   'Repayment'",
       |      "type": "string",
       |      "enum": [
       |        "CFSRP"
       |      ]
       |    },
       |    "repaymentAmount": {
       |      "description": "Mandatory. The amount to be repaid; min: 0.01, max: 999999999999999.99 (15 pound digits, 2   pence digits)",
       |      "type": "number",
       |      "multipleOf": 0.01,
       |      "minimum": 0.01,
       |      "maximum": 999999999999999.99
       |    },
       |    "taxRegime": {
       |      "type": "string",
       |      "description": "Mandatory. The tax regime of the case",
       |      "pattern": "^[a-zA-Z_]{2,15}$",
       |      "examples": [
       |        "VAT"
       |      ]
       |    },
       |    "taxPeriodStart": {
       |      "description": "Mandatory. The start of the tax period in the format CCYY-MM-DD",
       |      "$ref": "#/definitions/dateType"
       |    },
       |    "taxPeriodEnd": {
       |      "description": "Mandatory. The end of the tax period in the format CCYY-MM-DD",
       |      "$ref": "#/definitions/dateType"
       |    },
       |    "caseOwnerId": {
       |      "description": "Optional. The numeric id of the owner of the case.",
       |      "type": "number",
       |      "multipleOf": 1,
       |      "minimum": 1,
       |      "maximum": 9999999
       |    },
       |    "triggeredRiskRuleRef": {
       |      "description": "Optional. A   reference for the triggered risk rule on the source system",
       |      "type": "string",
       |      "pattern": "^[A-Za-z0-9\\$\\.\\-]{1,10}$",
       |      "examples": [
       |        "A-23"
       |      ]
       |    },
       |    "taxPayer": {
       |      "description": "Mandatory. All the details of the tax payer.",
       |      "type": "object",
       |      "additionalProperties": false,
       |      "required": [
       |        "taxPayerType",
       |        "referenceNumber",
       |        "addresses"
       |      ],
       |      "properties": {
       |        "taxPayerType": {
       |          "description": "Mandatory. A   categorisation of the tax payer e.g. Individual, Partnership, Organisation",
       |          "type": "string",
       |          "pattern": "^[A-Za-z0-9\\/\\-\\. ]{1,40}$"
       |        },
       |        "segment": {
       |          "description": "Optional. The segment of the tax payer e.g. Micro A; needed for an organistion type tax payer",
       |          "type": "string",
       |          "pattern": "^[A-Za-z0-9\\/\\- ]{1,40}$"
       |        },
       |        "nameDetails": {
       |          "description": "Optional. The name of the individual or organisation tax payer ",
       |          "oneOf": [
       |            {
       |              "type": "object",
       |              "additionalProperties": false,
       |              "required": [
       |                "firstName",
       |                "lastName"
       |              ],
       |              "properties": {
       |                "title": {
       |                  "description": "Optional. The person's title.",
       |                  "type": "string",
       |                  "enum": [
       |                    "NOT KNOWN",
       |                    "MR",
       |                    "MISS",
       |                    "MS",
       |                    "DR",
       |                    "REV",
       |                    "PROF"
       |                  ]
       |                },
       |                "firstName": {
       |                  "description": "Mandatory. The person's first name.",
       |                  "$ref": "#/definitions/nameType"
       |                },
       |                "lastName": {
       |                  "description": "Mandatory. The person's last name.",
       |                  "$ref": "#/definitions/nameType"
       |                }
       |              }
       |            },
       |            {
       |              "type": "object",
       |              "additionalProperties": false,
       |              "required": [
       |                "organisationName"
       |              ],
       |              "properties": {
       |                "organisationName": {
       |                  "description": "Mandatory. The organisation's name.",
       |                  "$ref": "#/definitions/organisationNameType"
       |                }
       |              }
       |            }
       |          ]
       |        },
       |        "referenceNumber": {
       |          "description": "Mandatory. A   key/value pair reference for the repayment.",
       |          "type": "object",
       |          "additionalProperties": false,
       |          "required": [
       |            "referenceType",
       |            "referenceValue"
       |          ],
       |          "properties": {
       |            "referenceType": {
       |              "description": "Mandatory. The type of the reference.",
       |              "type": "string",
       |              "pattern": "^[A-Za-z\\/\\-\\. ]{1,100}$"
       |            },
       |            "referenceValue": {
       |              "description": "Mandatory. The value of the reference.",
       |              "type": "string",
       |              "pattern": "^[A-Za-z0-9\\/\\-\\.]{5,25}$"
       |            }
       |          }
       |        },
       |        "addresses": {
       |          "description": "Mandatory. A   list of addresses of the tax payer",
       |          "type": "array",
       |          "minItems": 1,
       |          "maxItems": 50,
       |          "uniqueItems": true,
       |          "items": {
       |            "$ref": "#/definitions/addressEntry"
       |          }
       |        }
       |      }
       |    }
       |  },
       |  "definitions": {
       |    "dateType": {
       |      "description": "A date in the format CCYY-MM-DD. Earliest 1900-01-01, latest 2099-12-31, leap years accommodated.",
       |      "type": "string",
       |      "pattern": "^(((19|20)([2468][048]|[13579][26]|0[48])|2000)[\\-]02[\\-]29|((19|20)[0-9]{2}[\\-](0[469]|11)[\\-](0[1-9]|1[0-9]|2[0-9]|30)|(19|20)[0-9]{2}[\\-](0[13578]|1[02])[\\-](0[1-9]|[12][0-9]|3[01])|(19|20)[0-9]{2}[\\-]02[\\-](0[1-9]|1[0-9]|2[0-8])))$"
       |    },
       |    "nameType": {
       |      "type": "string",
       |      "pattern": "^[a-zA-Z &`\\-\\'\\.^]{1,35}$"
       |    },
       |    "organisationNameType": {
       |      "type": "string",
       |      "pattern": "^[a-zA-Z0-9 &`\\-\\'\\.^]{1,70}$"
       |    },
       |    "addressLineType": {
       |      "type": "string",
       |      "pattern": "^[A-Za-z0-9 \\-,.&'\\/]{1,35}$"
       |    },
       |    "addressEntry": {
       |      "type": "object",
       |      "additionalProperties": false,
       |      "required": [
       |        "addressLine1",
       |        "correspondenceAddress",
       |        "addressType"
       |      ],
       |      "properties": {
       |        "correspondenceAddress": {
       |          "description": "Mandatory. Whether this address is a   correspondence one (true) or not (false).",
       |          "type": "boolean"
       |        },
       |        "addressType": {
       |          "description": "Mandatory. The type of the address.",
       |          "type": "string",
       |          "pattern": "^[A-Za-z ]{4,20}$",
       |          "examples": [
       |            "Home",
       |            "Holiday house",
       |            "Business"
       |          ]
       |        },
       |        "addressLine1": {
       |          "description": "Mandatory. First line of the address.",
       |          "$ref": "#/definitions/addressLineType"
       |        },
       |        "addressLine2": {
       |          "description": "Optional. Second line of the address.",
       |          "$ref": "#/definitions/addressLineType"
       |        },
       |        "city": {
       |          "description": "Optional.",
       |          "type": "string",
       |          "pattern": "^[A-Za-z\\-\\'\\`\\&\\. ]{4,50}$"
       |        },
       |        "county": {
       |          "description": "Optional.",
       |          "type": "string",
       |          "pattern": "^[A-Za-z\\-\\'\\`\\&\\. ]{4,30}$"
       |        },
       |        "country": {
       |          "description": "Optional.",
       |          "type": "string",
       |          "pattern": "^[A-Za-z\\-\\'\\`\\&\\. ]{1,50}$"
       |        },
       |        "postcode": {
       |          "description": "Optional.",
       |          "type": "string",
       |          "pattern": "^[A-Z0-9 ]{3,10}$"
       |        }
       |      }
       |    }
       |  }
       |}
       |""".stripMargin

  val invalidCaseTypeUsingIncorrectDatatypeJson: String =
    """{
      | "sourceSystemId": "CNT",
      | "sourceSystemKey": "AZ1234567/7",
      | "case": {
      |   "caseType": 1234,
      |   "sourceSystemRef": "CFSRP",
      |   "repaymentAmount": 1234567.99,
      |   "taxRegime": "VAT_Trader",
      |   "taxPeriodStart": "2016-04-06",
      |   "taxPeriodEnd": "2017-04-05",
      |   "taxPayer": {
      |     "taxPayerType": "Individual",
      |     "nameDetails": {
      |       "title": "MR",
      |       "firstName": "John",
      |       "lastName": "Smith"
      |     },
      |     "referenceNumber": {
      |       "referenceType": "ValueAddedTax",
      |       "referenceValue": "VAT-666"
      |     },
      |     "addresses": [
      |       {
      |         "correspondenceAddress": true,
      |         "addressType": "Holiday house",
      |         "addressLine1": "1 Makebelieve Avenue"
      |       }
      |     ]
      |   }
      | }
      |}""".stripMargin

  val invalidCaseType: String =
    """{
      | "sourceSystemId": "CNT",
      | "sourceSystemKey": "AZ1234567/7",
      | "case": {
      |   "caseType": "AnInvalidCaseType",
      |   "sourceSystemRef": "CFSRP",
      |   "repaymentAmount": 1234567.99,
      |   "taxRegime": "VAT_Trader",
      |   "taxPeriodStart": "2016-04-06",
      |   "taxPeriodEnd": "2017-04-05",
      |   "taxPayer": {
      |     "taxPayerType": "Individual",
      |     "nameDetails": {
      |       "title": "MR",
      |       "firstName": "John",
      |       "lastName": "Smith"
      |     },
      |     "referenceNumber": {
      |       "referenceType": "ValueAddedTax",
      |       "referenceValue": "VAT-666"
      |     },
      |     "addresses": [
      |       {
      |         "correspondenceAddress": true,
      |         "addressType": "Holiday house",
      |         "addressLine1": "1 Makebelieve Avenue"
      |       }
      |     ]
      |   }
      | }
      |}""".stripMargin


  val caseflowCreateRiskCaseSchema: String =
    """|{
       |  "$schema": "http://json-schema.org/draft-04/schema#",
       |  "title": "API#1561 -  Create Case Request Schema v0.1.0",
       |  "type": "object",
       |  "additionalProperties": false,
       |  "required": [
       |    "caseType"
       |  ],
       |  "properties": {
       |    "caseType": {
       |      "type": "string",
       |      "enum": [
       |        "Risk"
       |      ]
       |    },
       |    "sourceSystemRef": {
       |      "description": "An identify reference; 'CFSRK' for a   'Risk'",
       |      "type": "string",
       |      "enum": [
       |        "CFSRK"
       |      ]
       |    }
       |  },
       |  "definitions": {
       |  }
       |}""".stripMargin

  val invalidRiskCaseJson: String =
    """{
      | "sourceSystemId": "CNT",
      | "sourceSystemKey": "AZ1234567/7",
      | "case": {
      |   "caseType": "Risk",
      |   "sourceSystemRef": "QWERT"
      | }
      |}""".stripMargin

  val invalidRepaymentCaseJson: String =
    s"""{
       |  "sourceSystemId": "CNT",
       |  "sourceSystemKey": "1234567890",
       |  "case": {
       |    "caseType": "Repayment",
       |    "sourceSystemRef": "CFSRP",
       |    "repaymentAmount": 1234567.99,
       |    "taxRegime": "VAT_Trader@",
       |    "taxPeriodEnd": "2017-04-05",
       |    "caseOwnerId": 666777,
       |    "triggeredRiskRuleRef": "Abc$$123",
       |    "taxPayer": {
       |      "taxPayerType": "Individual",
       |      "segment": "PlanB",
       |      "referenceNumber": {
       |        "referenceValue": "VAT-666"
       |      },
       |      "nameDetails": {
       |        "organisationName": "A Fake Organisation"
       |      },
       |      "addresses": [
       |        {
       |          "correspondenceAddress": true,
       |          "addressType": "Holidayhouse",
       |          "addressLine1": "1MakebelieveAvenue"
       |        }
       |      ]
       |    }
       |  }
       |}
       |""".stripMargin

  val requestJsonWithoutSourceSystemId: String =
    s"""{
       |  "sourceSystemKey": "1234567890",
       |  "case": {
       |    "caseType": "Repayment",
       |    "sourceSystemRef": "CFSRP",
       |    "repaymentAmount": 1234567.99,
       |    "taxRegime": "VAT_Trader",
       |    "taxPeriodStart": "2016-04-06",
       |    "taxPeriodEnd": "2017-04-05",
       |    "caseOwnerId": 666777,
       |    "triggeredRiskRuleRef": "Abc$$123",
       |    "taxPayer": {
       |      "taxPayerType": "Individual",
       |      "segment": "PlanB",
       |      "referenceNumber": {
       |        "referenceType": "ValueAddedTax",
       |        "referenceValue": "VAT-666"
       |      },
       |      "nameDetails": {
       |        "organisationName": "A Fake Organisation"
       |      },
       |      "addresses": [
       |        {
       |          "correspondenceAddress": true,
       |          "addressType": "Holidayhouse",
       |          "addressLine1": "1MakebelieveAvenue"
       |        }
       |      ]
       |    }
       |  }
       |}
       |""".stripMargin

}
