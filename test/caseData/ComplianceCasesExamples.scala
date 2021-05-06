/*
 * Copyright 2021 HM Revenue & Customs
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


object ComplianceCasesExamples {

  val exampleJsonSuccessResponse: String =
    """
      |{
      |  "Response": "CFSC",
      |  "Code": 202
      |}
      |""".stripMargin

  val exampleJsonErrorResponse: String = """{"errors":["object has missing required properties ([\"Case\"])"]}""".stripMargin

  val id = 150000

  val minimumRiskJson: String =
    """{
      |  "sourceSystemId": "CNT",
      |  "sourceSystemKey": "VI00004-1",
      |  "sourceSystemURL": "http://localhost:7052",
      |  "taxPayer": {
      |      "taxPayerType": "Individual",
      |      "segment": "Micro A",
      |      "nameDetails": {
      |        "title": "Professor",
      |        "firstName": "first",
      |        "lastName": "last"
      |      },
      |      "referenceNumbers": [
      |        {
      |          "referenceType": "VIRef",
      |          "referenceValue": "VI000002343Z"
      |        }
      |      ],
      |      "addresses": [
      |        {
      |          "correspondenceAddress": true,
      |          "addressType": "Registered Address",
      |          "addressLine1": "line 1",
      |          "addressLine2": "line 2",
      |          "city": "city",
      |          "county": "country",
      |          "postcode": "ZZ11 2YY"
      |        }
      |      ]
      |    },
      |  "case": {
      |    "caseType": "Risk",
      |    "sourceSystemRef": "CFSB",
      |    "campaignId": "CID-12345678",
      |    "projectId": "PID-12345678",
      |    "suggestedOfficerGrade": "Major",
      |    "interventionSubType": "Customer lead",
      |    "risks": [
      |      {
      |        "taxRegime": "ABSD",
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
      |          "potentialAmount": 9876500,
      |          "expectedAmount": 10008545,
      |          "emergingAmount": 10011456
      |        },
      |        "riskScore": 2.1
      |      }
      |    ]
      |  }
      |}""".stripMargin

  val minimumRepaymentOrganisationJson: String =
    """{
      | "sourceSystemId": "CNT",
      | "sourceSystemKey": "AZ1234567/7",
      | "sourceSystemURL": "http://localhost:7052",
      | "taxPayer": {
      |     "taxPayerType": "Organisation",
      |     "nameDetails": {
      |       "organisationName": "A Fake Organisation"
      |     },
      |     "referenceNumbers": [{
      |       "referenceType": "ValueAddedTax",
      |       "referenceValue": "VAT555"
      |     }],
      |    "addresses": [
      |     {
      |       "correspondenceAddress": true,
      |       "addressType": "Holiday house",
      |       "addressLine1": "1 A Fake Road"
      |     }
      |   ]
      | },
      | "case": {
      |   "caseType": "Repayment",
      |   "sourceSystemRef": "CFSRP",
      |   "repaymentAmount": 1234567,
      |   "taxRegime": "VAT_Trader",
      |   "taxPeriodStart": "2016-04-06",
      |   "taxPeriodEnd": "2017-04-05",
      |   "claimDate": "2017-04-05",
      |   "campaignId": "CID-00002344",
      |   "projectId": "PID-98765432"
      | }
      |}""".stripMargin

  val addressJson: String =
    s"""{
       | "sourceSystemId": "CNT",
       | "sourceSystemKey": "AZ1234567/7",
       |  "sourceSystemURL": "http://localhost:7052",
       |     "taxPayer": {
       |     "taxPayerType": "Organisation",
       |     "referenceNumbers": [{
       |       "referenceType": "ValueAddedTax",
       |       "referenceValue": "VAT666"
       |     }],
       |     "addresses": [
       |       {
       |         "correspondenceAddress": true,
       |         "addressType": "Home",
       |         "addressLine1": "1 A Fake Road",
       |         "addressLine2": "A Fake Place",
       |         "city": "A Fake City",
       |         "county": "A Fake County",
       |         "country": "A Fake Country",
       |         "postcode": "AB0 0CD"
       |       }
       |     ]
       |   },
       | "case": {
       |   "caseType": "Repayment",
       |   "sourceSystemRef": "CFSRP",
       |   "repaymentAmount": 1234567,
       |   "taxRegime": "VAT_Trader",
       |   "taxPeriodStart": "2016-04-06",
       |   "taxPeriodEnd": "2017-04-05",
       |   "claimDate": "2017-04-05",
       |   "campaignId": "CID-00002344",
       |   "projectId": "PID-98765432"
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
      |    "repaymentAmount": 123456,
      |    "taxRegime": "VATC",
      |    "taxPeriodStart": "2019-04-06",
      |    "taxPeriodEnd": "2020-04-05",
      |    "caseOwnerId": 666,
      |    "triggeredRiskRuleRef": "R0123",
      |    "oudn": "Bolton-2437-C",
      |    "claimDate": "2020-04-17"
      |  }
      |}
      |""".stripMargin

  val caseflowCreateCaseSchema: String =
    """{
      |  "$schema": "http://json-schema.org/draft-04/schema#",
      |  "title": "API#1561 -  Create Case Request Schema v0.1.0",
      |  "type": "object",
      |  "additionalProperties": false,
      |  "required": [
      |    "sourceSystemId",
      |    "sourceSystemKey",
      |    "sourceSystemURL",
      |    "taxPayer",
      |    "case"
      |  ],
      |  "properties": {
      |    "sourceSystemId": {
      |      "description": "Mandatory. To identify the source system. Currently only 'CNT' (Connect) supported",
      |      "type": "string",
      |      "enum": [
      |        "CNT"
      |      ]
      |    },
      |    "sourceSystemKey": {
      |      "description": "Mandatory. Primary key of a record on the source system",
      |      "type": "string",
      |      "pattern": "^[A-Za-z0-9/\\$\\.\\-]{1,50}$"
      |    },
      |    "sourceSystemURL": {
      |      "description": "Mandatory. Full URL of a record on the source system",
      |      "type": "string",
      |      "format": "uri",
      |      "minLength": 1,
      |      "maxLength": 255
      |    },
      |    "taxPayer": {
      |      "$ref": "#/definitions/taxPayerType"
      |    },
      |    "case": {
      |      "type": "object"
      |    }
      |  },
      |  "definitions": {
      |    "nameType": {
      |      "type": "string",
      |      "pattern": "^[a-zA-Z &`\\-\\'\\.^]{1,70}$"
      |    },
      |    "organisationNameType": {
      |      "type": "string",
      |      "description": "Allow Unicode blocks Basic Latin (Space to Tilde), printable Latin-1 Supplement (¡ to ÿ), and all of Latin Extended-A and Latin Extended-B and IP Extensions (Ā to ʯ), all the Latin Extended Additional characters (Ḁ to ỿ), General Punctuation characters Hyphen to Horizontal Bar (‐ to ―), General Punctuation characters (‘ to ‟), Currency Symbol characters (₠ to ₿), Letterlike Symbols Å and K",
      |      "pattern": "^[ -~¡-ÿĀ-ʯḀ-ỿ‐-―‘-‟₠-₿ÅK]{1,255}$"
      |    },
      |    "addressLineType": {
      |      "type": "string",
      |      "pattern": "^[A-Za-z0-9 \\-,\\.&\\'\\/]{1,35}$"
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
      |          "description": "Mandatory. Whether this address is a correspondence one (true) or not (false).",
      |          "type": "boolean"
      |        },
      |        "addressType": {
      |          "description": "Mandatory. The type of the address.",
      |          "type": "string",
      |          "pattern": "^[A-Za-z ]{4,20}$"
      |        },
      |        "addressLine1": {
      |          "$ref": "#/definitions/addressLineType"
      |        },
      |        "addressLine2": {
      |          "$ref": "#/definitions/addressLineType"
      |        },
      |        "city": {
      |          "description": "Optional.",
      |          "type": "string",
      |          "pattern": "^[A-Za-z\\-\\'\\`\\&\\. ]{2,35}$"
      |        },
      |        "county": {
      |          "description": "Optional.",
      |          "type": "string",
      |          "pattern": "^[A-Za-z\\-\\'\\`\\&\\. ]{2,35}$"
      |        },
      |        "country": {
      |          "description": "Optional.",
      |          "type": "string",
      |          "pattern": "^[A-Za-z\\-\\'\\`\\&\\. ]{1,35}$"
      |        },
      |        "postcode": {
      |          "description": "Optional. Standard UK postcode with optional space between the outcode and incode parts",
      |          "type": "string",
      |          "pattern": "^[A-Z]{1,2}[0-9][0-9A-Z]?\\s?[0-9][A-Z]{2}$"
      |        }
      |      }
      |    },
      |    "taxPayerType": {
      |      "description": "Mandatory. All the details of the tax payer.",
      |      "type": "object",
      |      "additionalProperties": false,
      |      "required": [
      |        "taxPayerType",
      |        "referenceNumbers",
      |        "addresses"
      |      ],
      |      "properties": {
      |        "taxPayerType": {
      |          "description": "Mandatory. A categorisation of the tax payer e.g. Individual, Partnership, Organisation",
      |          "type": "string",
      |          "pattern": "^[A-Za-z0-9\\/\\-\\. ]{1,40}$"
      |        },
      |        "segment": {
      |          "description": "Optional. The segment of the tax payer e.g. Micro A; needed\n    for an organisation type tax payer",
      |          "type": "string",
      |          "pattern": "^[A-Za-z0-9\\/\\- ]{1,60}$"
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
      |                    "Unknown",
      |                    "Mr",
      |                    "Mrs",
      |                    "Miss",
      |                    "Ms",
      |                    "Dr",
      |                    "Sir",
      |                    "Rev",
      |                    "Professor",
      |                    "Lord"
      |                  ]
      |                },
      |                "firstName": {
      |                  "$ref": "#/definitions/nameType"
      |                },
      |                "lastName": {
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
      |                  "$ref": "#/definitions/organisationNameType"
      |                }
      |              }
      |            }
      |          ]
      |        },
      |        "referenceNumbers": {
      |          "description": "Mandatory. An array of key/value pairs representing a reference for the customer.",
      |          "type": "array",
      |          "minItems": 1,
      |          "maxItems": 50,
      |          "uniqueItems": true,
      |          "items": {
      |            "type": "object",
      |            "additionalProperties": false,
      |            "required": [
      |              "referenceType",
      |              "referenceValue"
      |            ],
      |            "properties": {
      |              "referenceType": {
      |                "description": "Mandatory. The type of the reference.",
      |                "type": "string",
      |                "pattern": "^[A-Za-z\\/\\-\\. ]{1,100}$"
      |              },
      |              "referenceValue": {
      |                "description": "Mandatory. The value of the reference.",
      |                "type": "string",
      |                "pattern": "^[A-Z0-9\\/]{5,25}$"
      |              }
      |            }
      |          }
      |        },
      |        "addresses": {
      |          "description": "Mandatory. A list of addresses of the tax payer",
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
      |  }
      |}""".stripMargin

  val caseflowCreateRepaymentCaseSchema: String =
    """{
      |  "$schema": "http://json-schema.org/draft-04/schema#",
      |  "description": "Repayment Case",
      |  "type": "object",
      |  "additionalProperties": false,
      |  "required": [
      |    "caseType",
      |    "sourceSystemRef",
      |    "campaignId",
      |    "projectId",
      |    "repaymentAmount",
      |    "taxRegime",
      |    "taxPeriodStart",
      |    "taxPeriodEnd",
      |    "claimDate"
      |  ],
      |  "properties": {
      |    "caseType": {
      |      "description": "Mandatory. Must be 'Repayment' for a repayment case",
      |      "type": "string",
      |      "enum": [
      |        "Repayment"
      |      ]
      |    },
      |    "sourceSystemRef": {
      |      "description": "Mandatory. An identify reference; 'CFSRP' for a 'Repayment'",
      |      "type": "string",
      |      "enum": [
      |        "CFSRP"
      |      ]
      |    },
      |    "campaignId": {
      |      "$ref": "#/definitions/campaignIdType"
      |    },
      |    "projectId": {
      |      "$ref": "#/definitions/projectIdType"
      |    },
      |    "repaymentAmount": {
      |      "$ref": "#/definitions/amountPosType"
      |    },
      |    "taxRegime": {
      |      "$ref": "#/definitions/taxRegimeType"
      |    },
      |    "taxPeriodStart": {
      |      "$ref": "#/definitions/dateType"
      |    },
      |    "taxPeriodEnd": {
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
      |      "description": "Optional. A reference for the triggered risk rule on the source system",
      |      "type": "string",
      |      "pattern": "^R[0-9]{1,10}$"
      |    },
      |    "oudn": {
      |      "description": "Optional. OUDN of the owner",
      |      "type": "string",
      |      "pattern": "^[A-Za-z0-9\\&\\-\\'\\_]{8,100}$"
      |    },
      |    "claimDate": {
      |      "$ref": "#/definitions/dateType"
      |    },
      |    "inYearPeriod": {
      |      "description": "Optional. In Year Period, expected for VAT returns Only and upto 2 digits.",
      |      "type": "string",
      |      "pattern": "^[1-12]{1,2}$"
      |    },
      |    "firstLock": {
      |      "$ref": "#/definitions/lockType"
      |    },
      |    "secondLock": {
      |      "$ref": "#/definitions/lockType"
      |    },
      |    "thirdLock": {
      |      "$ref": "#/definitions/lockType"
      |    }
      |  },
      |  "definitions": {
      |    "campaignIdType": {
      |      "description": "The campaign id relating to this case",
      |      "type": "string",
      |      "pattern": "^CID-[0-9]{1,15}$"
      |    },
      |    "projectIdType": {
      |      "description": "The project id relating to this case",
      |      "type": "string",
      |      "pattern": "^PID-[0-9]{1,15}$"
      |    },
      |    "amountPosType": {
      |      "description": "A number representing a positive amount in pounds from 0 to 999999999999",
      |      "type": "number",
      |      "multipleOf": 1,
      |      "minimum": 0,
      |      "maximum": 999999999999
      |    },
      |    "taxRegimeType": {
      |      "type": "string",
      |      "description": "Mandatory. The tax regime of the Repayment case or of a risk for a Risk case",
      |      "pattern": "^[a-zA-Z_]{2,15}$"
      |    },
      |    "dateType": {
      |      "description": "A date in the format CCYY-MM-DD. Earliest 1900-01-01, latest 2099-12-31, leap years accommodated.",
      |      "type": "string",
      |      "pattern": "^(((19|20)([2468][048]|[13579][26]|0[48])|2000)[\\-]02[\\-]29|((19|20)[0-9]{2}[\\-](0[469]|11)[\\-](0[1-9]|1[0-9]|2[0-9]|30)|(19|20)[0-9]{2}[\\-](0[13578]|1[02])[\\-](0[1-9]|[12][0-9]|3[01])|(19|20)[0-9]{2}[\\-]02[\\-](0[1-9]|1[0-9]|2[0-8])))$"
      |    },
      |    "lockType": {
      |      "type": "string",
      |      "description": "Optional. The Lock for each repayment line up to 6 in length",
      |      "pattern": "^([a-zA-Z0-9]{1,6})$"
      |    }
      |  }
      |}""".stripMargin

  val invalidCaseTypeUsingIncorrectDatatypeJson: String =
    """{
      | "sourceSystemId": "CNT",
      | "sourceSystemURL": "http://localhost:7052",
      | "sourceSystemKey": "AZ1234567/7",
      |"taxPayer": {
      |     "taxPayerType": "Individual",
      |     "nameDetails": {
      |       "title": "Mr",
      |       "firstName": "FirstName",
      |       "lastName": "LastName"
      |     },
      |     "referenceNumbers": [{
      |       "referenceType": "ValueAddedTax",
      |       "referenceValue": "VAT555"
      |     }],
      |     "addresses": [
      |       {
      |         "correspondenceAddress": true,
      |         "addressType": "Holiday house",
      |         "addressLine1": "1 A Fake Road"
      |       }
      |     ]
      |   },
      | "case": {
      |   "caseType": 1234,
      |   "sourceSystemRef": "CFSRP",
      |   "repaymentAmount": 1234567.99,
      |   "taxRegime": "VAT_Trader",
      |   "taxPeriodStart": "2016-04-06",
      |   "taxPeriodEnd": "2017-04-05"
      | }
      |}""".stripMargin

  val invalidCaseType: String =
    """{
      | "sourceSystemId": "CNT",
      | "sourceSystemKey": "AZ1234567/7",
      | "sourceSystemURL": "http://localhost:7052",
      | "taxPayer": {
      |     "taxPayerType": "Individual",
      |     "nameDetails": {
      |       "title": "Mr",
      |       "firstName": "FirstName",
      |       "lastName": "LastName"
      |     },
      |     "referenceNumbers": [{
      |       "referenceType": "ValueAddedTax",
      |       "referenceValue": "VAT555"
      |     }],
      |     "addresses": [
      |       {
      |         "correspondenceAddress": true,
      |         "addressType": "Holiday house",
      |         "addressLine1": "1 A Fake Road"
      |       }
      |     ]
      |   },
      | "case": {
      |   "caseType": "AnInvalidCaseType",
      |   "sourceSystemRef": "CFSRP",
      |   "repaymentAmount": 1234567.99,
      |   "taxRegime": "VAT_Trader",
      |   "taxPeriodStart": "2016-04-06",
      |   "taxPeriodEnd": "2017-04-05"
      | }
      |}""".stripMargin


  val caseflowCreateRiskCaseSchema: String =
    """{
      |  "$schema": "http://json-schema.org/draft-04/schema#",
      |  "description": "Risk Case",
      |  "type": "object",
      |  "additionalProperties": false,
      |  "properties": {
      |    "caseType": {
      |      "description": "Mandatory. Has to be 'Risk' for a Risk case",
      |      "type": "string",
      |      "enum": [
      |        "Risk"
      |      ]
      |    },
      |    "sourceSystemRef": {
      |      "description": "Mandatory. An identify reference; 'CFSP' or 'CFSB' for a 'Risk'",
      |      "type": "string",
      |      "enum": [
      |        "CFSP",
      |        "CFSB"
      |      ]
      |    },
      |    "campaignId": {
      |      "$ref": "#/definitions/campaignIdType"
      |    },
      |    "projectId": {
      |      "$ref": "#/definitions/projectIdType"
      |    },
      |    "targetRIS": {
      |      "description": "Optional. The target RIS of the case",
      |      "type": "string",
      |      "pattern": "^[A-Za-z_]{2,50}$"
      |    },
      |    "complianceStream": {
      |      "description": "Optional.",
      |      "type": "string",
      |      "pattern": "^[A-Za-z0-9 \\.\\/\\&_-]{2,30}$"
      |    },
      |    "enquiryType": {
      |      "description": "Optional.",
      |      "type": "string",
      |      "pattern": "^[A-Za-z0-9 \\.\\/\\&_-]{2,30}$"
      |    },
      |    "vatOfficeCode": {
      |      "description": "Optional.",
      |      "type": "string",
      |      "pattern": "^[A-Za-z0-9 \\.\\/\\&_-]{1,20}$"
      |    },
      |    "lastDateForEnquiry": {
      |      "$ref": "#/definitions/dateType"
      |    },
      |    "confidenceScore": {
      |      "description": "Optional.",
      |      "type": "number",
      |      "multipleOf": 0.1,
      |      "minimum": 0,
      |      "maximum": 99.9
      |    },
      |    "archiveApproach": {
      |      "description": "Optional.",
      |      "type": "string",
      |      "pattern": "^[A-Za-z0-9 \\.\\/\\&_-]{1,100}$"
      |    },
      |    "interactionTitle": {
      |      "description": "Optional.",
      |      "type": "string",
      |      "pattern": "^[A-Za-z0-9 \\.\\/\\&_-]{1,100}$"
      |    },
      |    "authorisationType": {
      |      "description": "Optional.",
      |      "type": "string",
      |      "pattern": "^[A-Za-z0-9 \\.\\/\\&_-]{1,30}$"
      |    },
      |    "suggestedOfficerGrade": {
      |      "description": "Optional.",
      |      "type": "string",
      |      "pattern": "^[A-Za-z0-9 \\.\\/\\&_-]{1,20}$"
      |    },
      |    "interventionSubType": {
      |      "type": "string",
      |      "pattern": "^[A-Za-z0-9 \\.\\/\\&_-]{1,100}$"
      |    },
      |    "risks": {
      |      "description": "Optional. A collection of risks associated with the Risk case",
      |      "type": "array",
      |      "minItems": 1,
      |      "uniqueItems": true,
      |      "items": {
      |        "$ref": "#/definitions/riskEntry"
      |      }
      |    }
      |  },
      |  "required": [
      |    "caseType",
      |    "sourceSystemRef",
      |    "campaignId",
      |    "projectId",
      |    "suggestedOfficerGrade",
      |    "interventionSubType"
      |  ],
      |  "definitions": {
      |    "campaignIdType": {
      |      "description": "The campaign id relating to this case",
      |      "type": "string",
      |      "pattern": "^CID-[0-9]{1,15}$"
      |    },
      |    "projectIdType": {
      |      "description": "The project id relating to this case",
      |      "type": "string",
      |      "pattern": "^PID-[0-9]{1,15}$"
      |    },
      |    "dateType": {
      |      "description": "A date in the format CCYY-MM-DD. Earliest 1900-01-01, latest 2099-12-31, leap years accommodated.",
      |      "type": "string",
      |      "pattern": "^(((19|20)([2468][048]|[13579][26]|0[48])|2000)[\\-]02[\\-]29|((19|20)[0-9]{2}[\\-](0[469]|11)[\\-](0[1-9]|1[0-9]|2[0-9]|30)|(19|20)[0-9]{2}[\\-](0[13578]|1[02])[\\-](0[1-9]|[12][0-9]|3[01])|(19|20)[0-9]{2}[\\-]02[\\-](0[1-9]|1[0-9]|2[0-8])))$"
      |    },
      |    "riskEntry": {
      |      "description": "All the details of an identified risk",
      |      "type": "object",
      |      "additionalProperties": false,
      |      "required": [
      |        "taxRegime",
      |        "subRegime",
      |        "firstComplianceCheck",
      |        "secondComplianceCheck",
      |        "inaccuracyCategory",
      |        "inaccuracyDescription",
      |        "riskStartDate",
      |        "riskScore"
      |      ],
      |      "properties": {
      |        "taxRegime": {
      |          "$ref": "#/definitions/taxRegimeType"
      |        },
      |        "riskDescription": {
      |          "description": "Optional. A free text description of the risk",
      |          "type": "string",
      |          "pattern": "^[A-Za-z0-9 \\.\\/\\&_-]{1,100}$"
      |        },
      |        "subRegime": {
      |          "description": "Mandatory. A sub categorisation of the main tax regime",
      |          "type": "string",
      |          "pattern": "^[A-Za-z0-9_]{2,35}$"
      |        },
      |        "firstComplianceCheck": {
      |          "description": "Mandatory. Alphanumeric string with underscore separator",
      |          "type": "string",
      |          "pattern": "^[A-Za-z0-9_]{3,100}$"
      |        },
      |        "secondComplianceCheck": {
      |          "description": "Mandatory. Alphanumeric string with underscore separator",
      |          "type": "string",
      |          "pattern": "^[A-Za-z0-9_]{3,100}$"
      |        },
      |        "inaccuracyCategory": {
      |          "description": "Mandatory. Alphanumeric string with underscore separator",
      |          "type": "string",
      |          "pattern": "^[A-Za-z0-9_]{3,100}$"
      |        },
      |        "inaccuracyDescription": {
      |          "description": "Mandatory. Alphanumeric string with underscore separator",
      |          "type": "string",
      |          "pattern": "^[A-Za-z0-9_]{3,100}$"
      |        },
      |        "behaviours": {
      |          "description": "Optional. Contains the potential and emerging behaviours (both optional)",
      |          "type": "object",
      |          "additionalProperties": false,
      |          "properties": {
      |            "potentialBehaviour": {
      |              "description": "Optional.",
      |              "type": "string",
      |              "pattern": "^[A-Za-z0-9 \\.\\/\\&_-]{1,100}$"
      |            },
      |            "emergingBehaviour": {
      |              "description": "Optional.",
      |              "type": "string",
      |              "pattern": "^[A-Za-z0-9 \\.\\/\\&_-]{1,100}$"
      |            }
      |          }
      |        },
      |        "taxPeriodStart": {
      |          "$ref": "#/definitions/dateType"
      |        },
      |        "taxPeriodEnd": {
      |          "$ref": "#/definitions/dateType"
      |        },
      |        "riskStartDate": {
      |          "$ref": "#/definitions/dateType"
      |        },
      |        "amounts": {
      |          "description": "Optional. Contains the potential, expected, and emerging amounts (all optional)",
      |          "type": "object",
      |          "additionalProperties": false,
      |          "properties": {
      |            "potentialAmount": {
      |              "$ref": "#/definitions/amountPosNegType"
      |            },
      |            "expectedAmount": {
      |              "$ref": "#/definitions/amountPosNegType"
      |            },
      |            "emergingAmount": {
      |              "$ref": "#/definitions/amountPosNegType"
      |            }
      |          }
      |        },
      |        "riskScore": {
      |          "description": "Mandatory. The risk's score between 0.0 and 99.9",
      |          "type": "number",
      |          "multipleOf": 0.1,
      |          "minimum": 0,
      |          "maximum": 99.9
      |        }
      |      }
      |    },
      |    "taxRegimeType": {
      |      "type": "string",
      |      "description": "Mandatory. The tax regime of the Repayment case or of a risk for a Risk case",
      |      "pattern": "^[a-zA-Z_]{2,15}$"
      |    },
      |    "amountPosNegType": {
      |      "description": "A number representing a positive or negative amount in pounds from -999999999999 to 999999999999",
      |      "type": "number",
      |      "multipleOf": 1,
      |      "minimum": -999999999999,
      |      "maximum": 999999999999
      |    }
      |  }
      |}""".stripMargin

  val invalidRiskCaseJson: String =
    """{
      |  "sourceSystemId": "CNT",
      |  "sourceSystemKey": "VI00004-1",
      |  "sourceSystemURL": "http://localhost:7052",
      |  "taxPayer": {
      |      "taxPayerType": "Individual",
      |      "segment": "Micro A",
      |      "nameDetails": {
      |        "title": "Professor",
      |        "firstName": "first",
      |        "lastName": "last"
      |      },
      |      "referenceNumbers": [
      |        {
      |          "referenceType": "VIRef",
      |          "referenceValue": "VI000002343Z"
      |        }
      |      ],
      |      "addresses": [
      |        {
      |          "correspondenceAddress": true,
      |          "addressType": "Registered Address",
      |          "addressLine1": "line 1",
      |          "addressLine2": "line 2",
      |          "city": "city",
      |          "county": "country",
      |          "postcode": "ZZ11 2YY"
      |        }
      |      ]
      |    },
      |  "case": {
      |    "caseType": "Risk",
      |    "sourceSystemRef": "QWERT",
      |    "campaignId": "CID-12345678",
      |    "projectId": "PID-12345678",
      |    "suggestedOfficerGrade": "Major",
      |    "interventionSubType": "Customer lead",
      |    "risks": [
      |      {
      |        "taxRegime": "ABSD",
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
      |          "potentialAmount": 9876500,
      |          "expectedAmount": 10008545,
      |          "emergingAmount": 10011456
      |        },
      |        "riskScore": 2.1
      |      }
      |    ]
      |  }
      |}""".stripMargin

  val invalidRepaymentCaseJson: String =
    s"""{
       |  "sourceSystemId": "CNT",
       |  "sourceSystemKey": "1234567890",
       |  "sourceSystemURL": "http://localhost:7052",
       |  "taxPayer": {
       |    "taxPayerType": "Individual",
       |    "segment": "PlanB",
       |    "referenceNumbers": [{
       |      "referenceValue": "VAT123456"
       |    }],
       |    "nameDetails": {
       |      "organisationName": "A Fake Organisation"
       |    },
       |    "addresses": [
       |      {
       |        "correspondenceAddress": true,
       |        "addressType": "Holidayhouse",
       |        "addressLine1": "1 A Fake Road"
       |      }
       |    ]
       |  },
       |  "case": {
       |    "caseType": "Repayment",
       |    "sourceSystemRef": "CFSRP",
       |    "repaymentAmount": 1234567.99,
       |    "taxRegime": "VAT_Trader@",
       |    "taxPeriodEnd": "2017-04-05",
       |    "claimDate": "2017-04-05",
       |    "whoami": "unexpected",
       |    "repaymentAmount": 123456,
       |    "caseOwnerId": 666777,
       |    "triggeredRiskRuleRef": "R1234",
       |    "campaignId": "CID-00002344",
       |    "projectId": "PID-98765432"
       |  }
       |}
       |""".stripMargin

  lazy val multipleInvalidFieldsWithValidRiskCaseField: String =
    """
      |{
      | "sourceSystemId": "CNT",
      | "sourceSystemKey": [],
      | "sourceSystemURL": "http://me.com",
      | "case": {
      |    "caseType": "Risk",
      |    "sourceSystemRef": "CFSB",
      |    "campaignId": "CID-12345678",
      |    "projectId": "PID-12345678",
      |    "suggestedOfficerGrade": "Major",
      |    "interventionSubType": "Customer lead",
      |    "risks": [
      |      {
      |        "taxRegime": "ABSD",
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
      |          "potentialAmount": 9876500,
      |          "expectedAmount": 10008545,
      |          "emergingAmount": 10011456
      |        },
      |        "riskScore": 2.1
      |      }
      |    ]
      |  },
      | "love": "yelp"
      | }
      |""".stripMargin

  lazy val multipleInvalidFieldsWithValidRepaymentCaseField: String =
    """
      |{
      | "sourceSystemId": "CNT",
      | "sourceSystemKey": [],
      | "sourceSystemURL": "http://me.com",
      | "case": {
      |    "caseType": "Repayment",
      |    "sourceSystemRef": "CFSRP",
      |    "repaymentAmount": 1234567.99,
      |    "taxRegime": "VAT",
      |    "taxPeriodEnd": "2017-04-05",
      |    "taxPeriodStart": "2017-04-05",
      |    "claimDate": "2017-04-05",
      |    "repaymentAmount": 123456,
      |    "caseOwnerId": 666777,
      |    "triggeredRiskRuleRef": "R1234",
      |    "campaignId": "CID-00002344",
      |    "projectId": "PID-98765432"
      |  },
      | "love": "yelp"
      | }
      |""".stripMargin
}
