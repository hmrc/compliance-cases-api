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

import models.{CaseflowCase, ComplianceInvestigations, Risk, Risks, Taxpayer, Taxpayers}

object ComplianceCasesExamples {

  val exampleJsonSuccessResponse: String = """
                                              |{
                                              |  "Response" : "CFSC",
                                              |  "Code" : 202
                                              |}
                                              |""".stripMargin

  val exampleJsonErrorResponse: String = """{"errors":["object has missing required properties ([\"Case\"])"]}""".stripMargin

  val id = 150000

  val filledMinimumModel = ComplianceInvestigations(
      CaseflowCase(
        "CFSC", "474013587585 ", id, "CID-6269", "PID-6480", "YieldBearing ", None, None, None, None,
        "123456789 ", None, None, None, None, None, None, None, None, None,
        Risks(Risk("VAT ", "Example  ", 9.1 , "2008-04-06", "2009-04-05", None, None, None, None, None, None, None, None, None)),
        Taxpayers(Taxpayer("SoleTrader", None, None, None, None, None, None, None, None, None, None, None, None, None, None, None))
      )
    )

  val addressJson = s"""{
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
                       |        "Type": "SoleTrader",
                       |        "HomeAddress":{
                       |          "Line1": "Wow son this is a street for sure",
                       |          "Line2": "Damn 2nd liner too",
                       |          "Line3": "3 tho what bruh",
                       |          "Town": "Buddinge"
                       |        }
                       |      }
                       |    }
                       |  }
                       |}""".stripMargin

  val minimumJson: String = """{
                       |  "Case": {
                       |    "SourceSysRef": "CFSC",
                       |    "SourceSysID": "474013587585 ",
                       |    "CaseFlowID": 150000,
                       |    "CampaignID": "CID-6269",
                       |    "ProjectID": "PID-6480",
                       |    "CaseType": "YieldBearing ",
                       |    "VATOfficeCode": "123456789 ",
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

  val incorrectJson: String = """{
                         | "Invalid": "Invalid",
                         | "age": 54
                         |}""".stripMargin

  val fullCaseJson: String = """{
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

  val schema: String =
    """|{
       |  "definitions": {},
       |  "$schema": "http://json-schema.org/draft-07/schema#",
       |  "$id": "http://example.com/root.json",
       |  "type": "object",
       |  "title": "Caseflow Case Create Update Schema",
       |  "required": [
       |    "Case"
       |  ],
       |  "properties": {
       |    "Case": {
       |      "$id": "#/properties/Case",
       |      "type": "object",
       |      "required": [
       |        "SourceSysRef",
       |        "SourceSysID",
       |        "CaseFlowID",
       |        "CampaignID",
       |        "ProjectID",
       |        "CaseType",
       |        "VATOfficeCode",
       |        "Risks",
       |        "Taxpayers"
       |      ],
       |      "properties": {
       |        "SourceSysRef": {
       |          "$id": "#/properties/Case/properties/SourceSysRef",
       |          "type": "string",
       |          "examples": [
       |            "CFSC"
       |          ]
       |
       |        },
       |        "SourceSysID": {
       |          "$id": "#/properties/Case/properties/SourceSysID",
       |          "type": "string",
       |          "examples": [
       |            "991234010001"
       |          ]
       |
       |        },
       |        "CaseFlowID": {
       |          "$id": "#/properties/Case/properties/CaseFlowID",
       |          "type": "integer",
       |          "examples": [
       |            123456
       |          ]
       |        },
       |        "CampaignID": {
       |          "$id": "#/properties/Case/properties/CampaignID",
       |          "type": "string",
       |          "examples": [
       |            "123456"
       |          ]
       |        },
       |        "ProjectID": {
       |          "$id": "#/properties/Case/properties/ProjectID",
       |          "type": "string",
       |          "examples": [
       |            "9999"
       |          ]
       |        },
       |        "CaseType": {
       |          "$id": "#/properties/Case/properties/CaseType",
       |          "type": "string",
       |          "examples": [
       |            "YieldBearing"
       |          ]
       |        },
       |        "TargetRIS": {
       |          "$id": "#/properties/Case/properties/TargetRIS",
       |          "type": "string",
       |          "examples": [
       |            "VAT"
       |          ]
       |        },
       |        "ComplianceStream": {
       |          "$id": "#/properties/Case/properties/ComplianceStream",
       |          "type": "string",
       |          "examples": [
       |            "TEEL"
       |          ]
       |        },
       |        "EnguiryType": {
       |          "$id": "#/properties/Case/properties/EnguiryType",
       |          "type": "string",
       |          "examples": [
       |            "Full"
       |          ]
       |        },
       |        "Segment": {
       |          "$id": "#/properties/Case/properties/Segment",
       |          "type": "string",
       |          "examples": [
       |            "Medium"
       |          ]
       |        },
       |        "VATOfficeCode": {
       |          "$id": "#/properties/Case/properties/VATOfficeCode",
       |          "type": "string",
       |          "examples": [
       |            "123"
       |          ]
       |        },
       |        "ProjectedYield": {
       |          "$id": "#/properties/Case/properties/ProjectedYield",
       |          "type": "string",
       |          "examples": [
       |            "123400.00"
       |          ]
       |
       |        },
       |        "Filter1": {
       |          "$id": "#/properties/Case/properties/Filter1",
       |          "type": "string",
       |          "examples": [
       |            "string"
       |          ]
       |
       |        },
       |        "Filter2": {
       |          "$id": "#/properties/Case/properties/Filter2",
       |          "type": "string",
       |          "examples": [
       |            "string"
       |          ]
       |
       |        },
       |        "LastDateForEnquiry": {
       |          "$id": "#/properties/Case/properties/LastDateForEnquiry",
       |          "type": "string",
       |          "format": "date",
       |          "examples": [
       |            "2009-04-30"
       |          ]
       |
       |        },
       |        "ConfidenceScore": {
       |          "$id": "#/properties/Case/properties/ConfidenceScore",
       |          "type": "number",
       |          "examples": [
       |            "6.0"
       |          ]
       |
       |        },
       |        "ArchiveApproach": {
       |          "$id": "#/properties/Case/properties/ArchiveApproach",
       |          "type": "string",
       |          "examples": [
       |            "test"
       |          ]
       |
       |        },
       |        "InterventionSubType": {
       |          "$id": "#/properties/Case/properties/InterventionSubType",
       |          "type": "string",
       |          "examples": [
       |            "Compliance Return"
       |          ]
       |
       |        },
       |        "InteractionTitle": {
       |          "$id": "#/properties/Case/properties/InteractionTitle",
       |          "type": "string",
       |          "examples": [
       |            "String"
       |          ]
       |        },
       |        "AuthorisationType": {
       |          "$id": "#/properties/Case/properties/AuthorisationType",
       |          "type": "string",
       |          "examples": [
       |            "Mandatory"
       |          ]
       |
       |        },
       |        "Risks": {
       |          "$id": "#/properties/Case/properties/Risks",
       |          "type": "object",
       |          "required": [
       |            "Risk"
       |          ],
       |          "properties": {
       |            "Risk": {
       |              "$id": "#/properties/Case/properties/Risks/properties/Risk",
       |              "type": "object",
       |              "title": "The Risk Schema",
       |              "required": [
       |                "TaxRegime",
       |                "Description",
       |                "Score",
       |                "TaxPeriodFrom",
       |                "TaxPeriodTo"
       |              ],
       |              "properties": {
       |                "TaxRegime": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/TaxRegime",
       |                  "type": "string",
       |                  "examples": [
       |                    "VAT"
       |                  ]
       |                },
       |                "Description": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/Description",
       |                  "type": "string",
       |                  "examples": [
       |                    "string"
       |                  ]
       |
       |                },
       |                "Score": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/Score",
       |                  "type": "number",
       |                  "examples": [
       |                    "8.5"
       |                  ]
       |
       |                },
       |                "TaxPeriodFrom": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/TaxPeriodFrom",
       |                  "type": "string",
       |                  "format": "date",
       |                  "examples": [
       |                    "2008-04-6"
       |                  ]
       |
       |                },
       |                "TaxPeriodTo": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/TaxPeriodTo",
       |                  "type": "string",
       |                  "format": "date",
       |                  "examples": [
       |                    "2009-04-05"
       |                  ]
       |                },
       |                "SubRegime": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/SubRegime",
       |                  "type": "string",
       |                  "examples": [
       |                    "VAT"
       |                  ]
       |                },
       |                "ComplianceCheck1": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/ComplianceCheck1",
       |                  "type": "string",
       |                  "examples": [
       |                    "Nons9A12ACReview"
       |                  ]
       |                },
       |                "ComplianceCheck2": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/ComplianceCheck2",
       |                  "type": "string",
       |                  "examples": [
       |                    "AspectDiscov"
       |                  ]
       |                },
       |                "InaccuracyCategory": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/InaccuracyCategory",
       |                  "type": "string",
       |                  "examples": [
       |                    "Avoidance"
       |                  ]
       |                },
       |                "InaccuracyDescription": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/InaccuracyDescription",
       |                  "type": "string",
       |                  "examples": [
       |                    "Avoidance"
       |                  ]
       |
       |                },
       |                "PotentialAmount": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/PotentialAmount",
       |                  "type": "number",
       |                  "title": "The Potentialamount Schema",
       |
       |                  "examples": [
       |                    "100.00"
       |                  ]
       |                },
       |                "ExpectedAmount": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/ExpectedAmount",
       |                  "type": "number",
       |                  "examples": [
       |                    "100.00"
       |                  ]
       |
       |                },
       |                "PotentialBehaviour": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/PotentialBehaviour",
       |                  "type": "string",
       |                  "examples": [
       |                    "Mistake"
       |                  ]
       |
       |                },
       |                "EmergingBehaviour": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/EmergingBehaviour",
       |                  "type": "string",
       |                  "examples": [
       |                    "Mistake"
       |                  ]
       |
       |                }
       |              }
       |            }
       |          }
       |        },
       |        "Taxpayers": {
       |          "$id": "#/properties/Case/properties/Taxpayers",
       |          "type": "object",
       |          "required": [
       |            "Taxpayer"
       |          ],
       |          "properties": {
       |            "Taxpayer": {
       |              "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer",
       |              "type": "object",
       |              "required": [
       |                "Type"
       |              ],
       |              "properties": {
       |                "Type": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/Type",
       |                  "type": "string",
       |                  "examples": [
       |                    "SoleTrader"
       |                  ]
       |                },
       |                "OUID": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/OUID",
       |                  "type": "string",
       |                  "examples": [
       |                    "111"
       |                  ]
       |                },
       |                "Title": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/Title",
       |                  "type": "string",
       |                  "examples": [
       |                    "Mrs"
       |                  ]
       |                },
       |                "FirstName": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/FirstName",
       |                  "type": "string",
       |                  "examples": [
       |                    "String"
       |                  ]
       |                },
       |                "Surname": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/Surname",
       |                  "type": "string",
       |                  "examples": [
       |                    "Tax-Payer"
       |                  ]
       |                },
       |                "NINO": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/NINO",
       |                  "type": "string",
       |                  "examples": [
       |                    "WL1234567890"
       |                  ]
       |                },
       |                "UTR": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/UTR",
       |                  "type": "string",
       |                  "examples": [
       |                    "string"
       |                  ]
       |                },
       |                "EmpPAYERef": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/EmpPAYERef",
       |                  "type": "string",
       |                  "examples": [
       |                    "string"
       |                  ]
       |                },
       |                "VATNumber": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/VATNumber",
       |                  "type": "string",
       |                  "examples": [
       |                    "string"
       |                  ]
       |                },
       |                "Reference": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/Reference",
       |                  "type": "string",
       |                  "examples": [
       |                    "string"
       |                  ]
       |                },
       |                "TypeOfReference": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/TypeOfReference",
       |                  "type": "string",
       |                  "maxLength": 15,
       |                  "examples": [
       |                    "VatRegistrationNumber"
       |                  ]
       |                },
       |                "ValueOfReference": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/ValueOfReference",
       |                  "type": "string",
       |                  "examples": [
       |                    "Other"
       |                  ]
       |                },
       |                "HomeAddress": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/HomeAddress",
       |                  "type": "object",
       |                  "properties": {
       |                    "Line1": {
       |                      "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/HomeAddress/properties/Line1",
       |                      "type": "string",
       |                      "examples": [
       |                        "2, The Tree line cul de sac"
       |                      ]
       |                    },
       |                    "Line2": {
       |                      "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/HomeAddress/properties/Line2",
       |                      "type": "string",
       |                      "examples": [
       |                        "string"
       |                      ]
       |                    },
       |                    "Line3": {
       |                      "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/HomeAddress/properties/Line3",
       |                      "type": "string",
       |                      "examples": [
       |                        "string"
       |                      ]
       |                    },
       |                    "Town": {
       |                      "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/HomeAddress/properties/Town",
       |                      "type": "string",
       |                      "examples": [
       |                        "nice suburb"
       |                      ]
       |                    },
       |                    "Postcode": {
       |                      "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/HomeAddress/properties/Postcode",
       |                      "type": "string",
       |                      "examples": [
       |                        "ns1 1pc"
       |                      ]
       |                    },
       |                    "Phone": {
       |                      "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/HomeAddress/properties/Phone",
       |                      "type": "string",
       |                      "examples": [
       |                        "123-123-1234"
       |                      ]
       |                    }
       |                  }
       |                },
       |                "BusinessName": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/BusinessName",
       |                  "type": "string",
       |                  "examples": [
       |                    "The Village Pub"
       |                  ]
       |                },
       |                "BusinessAddress": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/BusinessAddress",
       |                  "type": "object",
       |                  "properties": {
       |                    "Line1": {
       |                      "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/BusinessAddress/properties/Line1",
       |                      "type": "string",
       |                      "examples": [
       |                        "1, The High Street"
       |                      ]
       |                    },
       |                    "Line2": {
       |                      "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/BusinessAddress/properties/Line2",
       |                      "type": "string",
       |                      "examples": [
       |                        "string"
       |                      ]
       |                    },
       |                    "Line3": {
       |                      "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/BusinessAddress/properties/Line3",
       |                      "type": "string",
       |                      "examples": [
       |                        "string"
       |                      ]
       |                    },
       |                    "Town": {
       |                      "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/BusinessAddress/properties/Town",
       |                      "type": "string",
       |                      "examples": [
       |                        "Niceville"
       |                      ]
       |                    },
       |                    "Postcode": {
       |                      "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/BusinessAddress/properties/Postcode",
       |                      "type": "string",
       |                      "examples": [
       |                        "PP1 1YZ"
       |                      ]
       |                    },
       |                    "Phone": {
       |                      "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/BusinessAddress/properties/Phone",
       |                      "type": "string",
       |                      "examples": [
       |                        "0123456"
       |                      ]
       |                    }
       |                  }
       |                },
       |                "email": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/email",
       |                  "type": "string",
       |                  "examples": [
       |                    "tax.payer@yahoo.com"
       |                  ]
       |
       |                }
       |              }
       |            }
       |          }
       |        }
       |      }
       |    }
       |  }
       |}
       |""".stripMargin

  val testSchema: String =
    """|{
       |  "definitions": {},
       |  "$schema": "http://json-schema.org/draft-07/schema#",
       |  "$id": "http://example.com/root.json",
       |  "type": "object",
       |  "title": "Caseflow Case Create Update Schema",
       |  "required": [
       |    "Case"
       |  ],
       |  "properties": {
       |    "Case": {
       |      "$id": "#/properties/Case",
       |      "type": "object",
       |      "required": [
       |        "SourceSysRef",
       |        "SourceSysID",
       |        "CaseFlowID",
       |        "CampaignID",
       |        "ProjectID",
       |        "CaseType",
       |        "VATOfficeCode",
       |        "Risks",
       |        "Taxpayers"
       |      ],
       |      "properties": {
       |        "SourceSysRef": {
       |          "$id": "#/properties/Case/properties/SourceSysRef",
       |          "type": "string",
       |          "examples": [
       |            "CFSC"
       |          ]
       |
       |        },
       |        "SourceSysID": {
       |          "$id": "#/properties/Case/properties/SourceSysID",
       |          "type": "string",
       |          "examples": [
       |            "991234010001"
       |          ]
       |
       |        },
       |        "CaseFlowID": {
       |          "$id": "#/properties/Case/properties/CaseFlowID",
       |          "type": "integer",
       |          "examples": [
       |            123456
       |          ]
       |        },
       |        "CampaignID": {
       |          "$id": "#/properties/Case/properties/CampaignID",
       |          "type": "integer",
       |          "examples": [
       |            123456
       |          ]
       |        },
       |        "ProjectID": {
       |          "$id": "#/properties/Case/properties/ProjectID",
       |          "type": "string",
       |          "examples": [
       |            "9999"
       |          ]
       |        },
       |        "CaseType": {
       |          "$id": "#/properties/Case/properties/CaseType",
       |          "type": "string",
       |          "examples": [
       |            "YieldBearing"
       |          ]
       |        },
       |        "TargetRIS": {
       |          "$id": "#/properties/Case/properties/TargetRIS",
       |          "type": "string",
       |          "examples": [
       |            "VAT"
       |          ]
       |        },
       |        "ComplianceStream": {
       |          "$id": "#/properties/Case/properties/ComplianceStream",
       |          "type": "string",
       |          "examples": [
       |            "TEEL"
       |          ]
       |        },
       |        "EnguiryType": {
       |          "$id": "#/properties/Case/properties/EnguiryType",
       |          "type": "string",
       |          "examples": [
       |            "Full"
       |          ]
       |        },
       |        "Segment": {
       |          "$id": "#/properties/Case/properties/Segment",
       |          "type": "string",
       |          "examples": [
       |            "Medium"
       |          ]
       |        },
       |        "VATOfficeCode": {
       |          "$id": "#/properties/Case/properties/VATOfficeCode",
       |          "type": "string",
       |          "examples": [
       |            "123"
       |          ]
       |        },
       |        "ProjectedYield": {
       |          "$id": "#/properties/Case/properties/ProjectedYield",
       |          "type": "string",
       |          "examples": [
       |            "123400.00"
       |          ]
       |
       |        },
       |        "Filter1": {
       |          "$id": "#/properties/Case/properties/Filter1",
       |          "type": "string",
       |          "examples": [
       |            "string"
       |          ]
       |
       |        },
       |        "Filter2": {
       |          "$id": "#/properties/Case/properties/Filter2",
       |          "type": "string",
       |          "examples": [
       |            "string"
       |          ]
       |
       |        },
       |        "LastDateForEnquiry": {
       |          "$id": "#/properties/Case/properties/LastDateForEnquiry",
       |          "type": "string",
       |          "format": "date",
       |          "examples": [
       |            "2009-04-30"
       |          ]
       |
       |        },
       |        "ConfidenceScore": {
       |          "$id": "#/properties/Case/properties/ConfidenceScore",
       |          "type": "number",
       |          "examples": [
       |            "6.0"
       |          ]
       |
       |        },
       |        "ArchiveApproach": {
       |          "$id": "#/properties/Case/properties/ArchiveApproach",
       |          "type": "string",
       |          "examples": [
       |            "test"
       |          ]
       |
       |        },
       |        "InterventionSubType": {
       |          "$id": "#/properties/Case/properties/InterventionSubType",
       |          "type": "string",
       |          "examples": [
       |            "Compliance Return"
       |          ]
       |
       |        },
       |        "InteractionTitle": {
       |          "$id": "#/properties/Case/properties/InteractionTitle",
       |          "type": "string",
       |          "examples": [
       |            "String"
       |          ]
       |        },
       |        "AuthorisationType": {
       |          "$id": "#/properties/Case/properties/AuthorisationType",
       |          "type": "string",
       |          "examples": [
       |            "Mandatory"
       |          ]
       |
       |        },
       |        "Risks": {
       |          "$id": "#/properties/Case/properties/Risks",
       |          "type": "object",
       |          "required": [
       |            "Risk"
       |          ],
       |          "properties": {
       |            "Risk": {
       |              "$id": "#/properties/Case/properties/Risks/properties/Risk",
       |              "type": "object",
       |              "title": "The Risk Schema",
       |              "required": [
       |                "TaxRegime",
       |                "Description",
       |                "Score",
       |                "TaxPeriodFrom",
       |                "TaxPeriodTo"
       |              ],
       |              "properties": {
       |                "TaxRegime": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/TaxRegime",
       |                  "type": "string",
       |                  "examples": [
       |                    "VAT"
       |                  ]
       |                },
       |                "Description": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/Description",
       |                  "type": "string",
       |                  "examples": [
       |                    "string"
       |                  ]
       |
       |                },
       |                "Score": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/Score",
       |                  "type": "number",
       |                  "examples": [
       |                    "8.5"
       |                  ]
       |
       |                },
       |                "TaxPeriodFrom": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/TaxPeriodFrom",
       |                  "type": "string",
       |                  "format": "date",
       |                  "examples": [
       |                    "2008-04-6"
       |                  ]
       |
       |                },
       |                "TaxPeriodTo": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/TaxPeriodTo",
       |                  "type": "string",
       |                  "format": "date",
       |                  "examples": [
       |                    "2009-04-05"
       |                  ]
       |                },
       |                "SubRegime": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/SubRegime",
       |                  "type": "string",
       |                  "examples": [
       |                    "VAT"
       |                  ]
       |                },
       |                "ComplianceCheck1": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/ComplianceCheck1",
       |                  "type": "string",
       |                  "examples": [
       |                    "Nons9A12ACReview"
       |                  ]
       |                },
       |                "ComplianceCheck2": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/ComplianceCheck2",
       |                  "type": "string",
       |                  "examples": [
       |                    "AspectDiscov"
       |                  ]
       |                },
       |                "InaccuracyCategory": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/InaccuracyCategory",
       |                  "type": "string",
       |                  "examples": [
       |                    "Avoidance"
       |                  ]
       |                },
       |                "InaccuracyDescription": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/InaccuracyDescription",
       |                  "type": "string",
       |                  "examples": [
       |                    "Avoidance"
       |                  ]
       |
       |                },
       |                "PotentialAmount": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/PotentialAmount",
       |                  "type": "number",
       |                  "title": "The Potentialamount Schema",
       |
       |                  "examples": [
       |                    "100.00"
       |                  ]
       |                },
       |                "ExpectedAmount": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/ExpectedAmount",
       |                  "type": "number",
       |                  "examples": [
       |                    "100.00"
       |                  ]
       |
       |                },
       |                "PotentialBehaviour": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/PotentialBehaviour",
       |                  "type": "string",
       |                  "examples": [
       |                    "Mistake"
       |                  ]
       |
       |                },
       |                "EmergingBehaviour": {
       |                  "$id": "#/properties/Case/properties/Risks/properties/Risk/properties/EmergingBehaviour",
       |                  "type": "string",
       |                  "examples": [
       |                    "Mistake"
       |                  ]
       |
       |                }
       |              }
       |            }
       |          }
       |        },
       |        "Taxpayers": {
       |          "$id": "#/properties/Case/properties/Taxpayers",
       |          "type": "object",
       |          "required": [
       |            "Taxpayer"
       |          ],
       |          "properties": {
       |            "Taxpayer": {
       |              "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer",
       |              "type": "object",
       |              "required": [
       |                "Type"
       |              ],
       |              "properties": {
       |                "Type": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/Type",
       |                  "type": "string",
       |                  "examples": [
       |                    "SoleTrader"
       |                  ]
       |                },
       |                "OUID": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/OUID",
       |                  "type": "string",
       |                  "examples": [
       |                    "111"
       |                  ]
       |                },
       |                "Title": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/Title",
       |                  "type": "string",
       |                  "examples": [
       |                    "Mrs"
       |                  ]
       |                },
       |                "FirstName": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/FirstName",
       |                  "type": "string",
       |                  "examples": [
       |                    "String"
       |                  ]
       |                },
       |                "Surname": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/Surname",
       |                  "type": "string",
       |                  "examples": [
       |                    "Tax-Payer"
       |                  ]
       |                },
       |                "NINO": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/NINO",
       |                  "type": "string",
       |                  "examples": [
       |                    "WL1234567890"
       |                  ]
       |                },
       |                "UTR": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/UTR",
       |                  "type": "string",
       |                  "examples": [
       |                    "string"
       |                  ]
       |                },
       |                "EmpPAYERef": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/EmpPAYERef",
       |                  "type": "string",
       |                  "examples": [
       |                    "string"
       |                  ]
       |                },
       |                "VATNumber": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/VATNumber",
       |                  "type": "string",
       |                  "examples": [
       |                    "string"
       |                  ]
       |                },
       |                "Reference": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/Reference",
       |                  "type": "string",
       |                  "examples": [
       |                    "string"
       |                  ]
       |                },
       |                "TypeOfReference": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/TypeOfReference",
       |                  "type": "string",
       |                  "maxLength": 15,
       |                  "examples": [
       |                    "VatRegistrationNumber"
       |                  ]
       |                },
       |                "ValueOfReference": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/ValueOfReference",
       |                  "type": "string",
       |                  "examples": [
       |                    "Other"
       |                  ]
       |                },
       |                "HomeAddress": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/HomeAddress",
       |                  "type": "object",
       |                  "properties": {
       |                    "Line1": {
       |                      "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/HomeAddress/properties/Line1",
       |                      "type": "string",
       |                      "examples": [
       |                        "2, The Tree line cul de sac"
       |                      ]
       |                    },
       |                    "Line2": {
       |                      "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/HomeAddress/properties/Line2",
       |                      "type": "string",
       |                      "examples": [
       |                        "string"
       |                      ]
       |                    },
       |                    "Line3": {
       |                      "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/HomeAddress/properties/Line3",
       |                      "type": "string",
       |                      "examples": [
       |                        "string"
       |                      ]
       |                    },
       |                    "Town": {
       |                      "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/HomeAddress/properties/Town",
       |                      "type": "string",
       |                      "examples": [
       |                        "nice suburb"
       |                      ]
       |                    },
       |                    "Postcode": {
       |                      "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/HomeAddress/properties/Postcode",
       |                      "type": "string",
       |                      "examples": [
       |                        "ns1 1pc"
       |                      ]
       |                    },
       |                    "Phone": {
       |                      "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/HomeAddress/properties/Phone",
       |                      "type": "string",
       |                      "examples": [
       |                        "123-123-1234"
       |                      ]
       |                    }
       |                  }
       |                },
       |                "BusinessName": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/BusinessName",
       |                  "type": "string",
       |                  "examples": [
       |                    "The Village Pub"
       |                  ]
       |                },
       |                "BusinessAddress": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/BusinessAddress",
       |                  "type": "object",
       |                  "properties": {
       |                    "Line1": {
       |                      "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/BusinessAddress/properties/Line1",
       |                      "type": "string",
       |                      "examples": [
       |                        "1, The High Street"
       |                      ]
       |                    },
       |                    "Line2": {
       |                      "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/BusinessAddress/properties/Line2",
       |                      "type": "string",
       |                      "examples": [
       |                        "string"
       |                      ]
       |                    },
       |                    "Line3": {
       |                      "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/BusinessAddress/properties/Line3",
       |                      "type": "string",
       |                      "examples": [
       |                        "string"
       |                      ]
       |                    },
       |                    "Town": {
       |                      "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/BusinessAddress/properties/Town",
       |                      "type": "string",
       |                      "examples": [
       |                        "Niceville"
       |                      ]
       |                    },
       |                    "Postcode": {
       |                      "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/BusinessAddress/properties/Postcode",
       |                      "type": "string",
       |                      "examples": [
       |                        "PP1 1YZ"
       |                      ]
       |                    },
       |                    "Phone": {
       |                      "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/BusinessAddress/properties/Phone",
       |                      "type": "string",
       |                      "examples": [
       |                        "0123456"
       |                      ]
       |                    }
       |                  }
       |                },
       |                "email": {
       |                  "$id": "#/properties/Case/properties/Taxpayers/properties/Taxpayer/properties/email",
       |                  "type": "string",
       |                  "examples": [
       |                    "tax.payer@yahoo.com"
       |                  ]
       |
       |                }
       |              }
       |            }
       |          }
       |        }
       |      }
       |    }
       |  }
       |}
       |""".stripMargin

  val testJson2: String =
    """{
      |     "Case": {
      |           "SourceSysRef": "CFSC",
      |           "SourceSysID": "474013587585 ",
      |           "CaseFlowID": 150000,
      |           "CampaignID": 6269,
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

}
