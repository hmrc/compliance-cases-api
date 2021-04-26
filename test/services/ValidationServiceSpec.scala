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

package services

import caseData.ComplianceCasesExamples._
import controllers.actions.RequestWithCorrelationId
import helpers.MockHelpers
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.{JsNull, JsString, Json}
import play.api.mvc.AnyContent
import play.api.test.FakeRequest
import uk.gov.hmrc.http.HeaderCarrier

class ValidationServiceSpec extends PlaySpec with ScalaFutures with IntegrationPatience with MockHelpers {

  implicit lazy val hc: HeaderCarrier = HeaderCarrier(sessionId = None)


  implicit val request: RequestWithCorrelationId[AnyContent] = RequestWithCorrelationId(FakeRequest(), "Some-Correlation-Id")

  def validationService = new ValidationService(mockResourceService)

  "validationService" should {

    "return None if json is a valid repayment type" in {
      Given.the.resourceService.returnsResourceAt("/schemas/caseflowCreateRepaymentCaseSchema.json", caseflowCreateRepaymentCaseSchema).build()
      validationService.validateAndRetrieveErrors(caseflowCreateCaseSchema, Json.parse(minimumRepaymentOrganisationJson)) mustBe None
    }
    "return None if json is a valid full repayment type" in {
      Given.the.resourceService.returnsResourceAt("/schemas/caseflowCreateRepaymentCaseSchema.json", caseflowCreateRepaymentCaseSchema).build()
      validationService.validateAndRetrieveErrors(caseflowCreateCaseSchema, Json.parse(fullCaseJson)) mustBe None
    }
    "return None if json is a valid repayment type with address" in {
      Given.the.resourceService.returnsResourceAt("/schemas/caseflowCreateRepaymentCaseSchema.json", caseflowCreateRepaymentCaseSchema).build()
      validationService.validateAndRetrieveErrors(caseflowCreateCaseSchema, Json.parse(addressJson)) mustBe None
    }
    "return None if json is a valid risk type" in {
      Given.the.resourceService.returnsResourceAt("/schemas/caseflowCreateRiskCaseSchema.json", caseflowCreateRiskCaseSchema).build()
      validationService.validateAndRetrieveErrors(caseflowCreateCaseSchema, Json.parse(minimumRiskJson)) mustBe None
    }

    "return json schema errors for json that is not an object" in {
      validationService.validateAndRetrieveErrors(caseflowCreateCaseSchema,
        JsString(""))
        .get mustBe Json.parse(
        """
          |{
          | "code": "INVALID_PAYLOAD",
          | "message": "Submission has not passed validation. Invalid payload.",
          | "errors":
          | [
          |   {"code":"INVALID_JSON_TYPE","message":"Invalid Json type as payload","path":""}
          | ]
          |}
          |""".stripMargin)
    }

    "return json schema errors if no Json (JsNull) is passed in" in {
      validationService.validateAndRetrieveErrors(caseflowCreateCaseSchema, JsNull)
        .get mustBe Json.parse(
        """
          |{
          | "code": "INVALID_PAYLOAD",
          | "message": "Submission has not passed validation. Invalid payload.",
          | "errors":
          | [
          |   {"code":"INVALID_JSON_TYPE","message":"Invalid Json type as payload","path":""}
          | ]
          |}
          |""".stripMargin)
    }

    "return json schema errors for multiple errors and no case" in {
      validationService.validateAndRetrieveErrors(caseflowCreateCaseSchema,
        Json.parse("""{"sourceSystemId": "CNT", "sourceSystemKey": [], "sourceSystemURL": "http://me.com", "case": [], "love": "yelp"}"""))
        .get mustBe Json.parse(
        """
          |{
          | "code": "INVALID_PAYLOAD",
          | "message": "Submission has not passed validation. Invalid payload.",
          | "errors":
          | [
          |   {"code":"UNEXPECTED_FIELD","message":"Unexpected field found","path":"/love"},
          |   {"code":"MISSING_FIELD","message":"Expected field not present","path":"/taxPayer"},
          |   {"code":"INVALID_FIELD","message":"Invalid value in field","path":"/case"},
          |   {"code":"INVALID_FIELD","message":"Invalid value in field","path":"/sourceSystemKey"}
          | ]
          |}
          |""".stripMargin)
    }

    "return json schema errors for multiple errors and valid risk case field" in {
      Given.the.resourceService.returnsResourceAt("/schemas/caseflowCreateRiskCaseSchema.json", caseflowCreateRiskCaseSchema).build()
      validationService.validateAndRetrieveErrors(caseflowCreateCaseSchema,
        Json.parse(multipleInvalidFieldsWithValidRiskCaseField))
        .get mustBe Json.parse(
        """
          |{
          | "code": "INVALID_PAYLOAD",
          | "message": "Submission has not passed validation. Invalid payload.",
          | "errors":
          | [
          |   {"code":"UNEXPECTED_FIELD","message":"Unexpected field found","path":"/love"},
          |   {"code":"MISSING_FIELD","message":"Expected field not present","path":"/taxPayer"},
          |   {"code":"INVALID_FIELD","message":"Invalid value in field","path":"/sourceSystemKey"}
          | ]
          |}
          |""".stripMargin)
    }

    "return json schema errors for multiple errors and valid repayment case field" in {
      Given.the.resourceService.returnsResourceAt("/schemas/caseflowCreateRepaymentCaseSchema.json", caseflowCreateRepaymentCaseSchema).build()
      validationService.validateAndRetrieveErrors(caseflowCreateCaseSchema,
        Json.parse(multipleInvalidFieldsWithValidRepaymentCaseField))
        .get mustBe Json.parse(
        """
          |{
          | "code": "INVALID_PAYLOAD",
          | "message": "Submission has not passed validation. Invalid payload.",
          | "errors":
          | [
          |   {"code":"UNEXPECTED_FIELD","message":"Unexpected field found","path":"/love"},
          |   {"code":"MISSING_FIELD","message":"Expected field not present","path":"/taxPayer"},
          |   {"code":"INVALID_FIELD","message":"Invalid value in field","path":"/sourceSystemKey"}
          | ]
          |}
          |""".stripMargin)
    }

    "return invalid fields in risk" in {
      Given.the.resourceService.returnsResourceAt("/schemas/caseflowCreateRiskCaseSchema.json", caseflowCreateRiskCaseSchema).build()
      validationService.validateAndRetrieveErrors(caseflowCreateCaseSchema,
        Json.parse(invalidRiskCaseJson)).get mustBe Json.parse(
        """
          |{
          | "code": "INVALID_PAYLOAD",
          | "message": "Submission has not passed validation for the Risk model. Invalid payload.",
          | "errors":
          | [
          |   {"code":"INVALID_FIELD","message":"Invalid value in field","path":"/case/sourceSystemRef"}
          | ]
          |}
          |""".stripMargin
      )
    }

    "return missing and invalid fields in repayments" in {
      Given.the.resourceService.returnsResourceAt("/schemas/caseflowCreateRepaymentCaseSchema.json", caseflowCreateRepaymentCaseSchema).build()

      validationService.validateAndRetrieveErrors(caseflowCreateCaseSchema,
        Json.parse(invalidRepaymentCaseJson)).get mustBe Json.parse(
        """
          |{
          | "code": "INVALID_PAYLOAD",
          | "message": "Submission has not passed validation for the Repayment model. Invalid payload.",
          | "errors":
          | [
          |   {"code":"MISSING_FIELD","message":"Expected field not present","path":"/taxPayer/referenceNumbers/0/referenceType"},
          |   {"code":"UNEXPECTED_FIELD","message":"Unexpected field found","path":"/case/whoami"},
          |   {"code":"MISSING_FIELD","message":"Expected field not present","path":"/case/taxPeriodStart"},
          |   {"code":"INVALID_FIELD","message":"Invalid value in field","path":"/case/taxRegime"}
          | ]
          |}
          |""".stripMargin
      )
    }

    "return invalid caseType using incorrect datatype" in {
      validationService.validateAndRetrieveErrors(caseflowCreateCaseSchema,
        Json.parse(invalidCaseTypeUsingIncorrectDatatypeJson)).get mustBe Json.parse(
        """
          |{
          | "code": "INVALID_PAYLOAD",
          | "message": "Submission has not passed validation. Invalid payload.",
          | "errors":
          | [
          |   {"code":"INVALID_FIELD","message":"Invalid value in field","path":"/case/caseType"}
          | ]
          |}
          |""".stripMargin
      )
    }

    "return invalid caseType" in {
      validationService.validateAndRetrieveErrors(caseflowCreateCaseSchema,
        Json.parse(invalidCaseType)).get mustBe Json.parse(
        """
          |{
          | "code": "INVALID_PAYLOAD",
          | "message": "Submission has not passed validation. Invalid payload.",
          | "errors":
          | [
          |   {"code":"INVALID_FIELD","message":"Invalid value in field","path":"/case/caseType"}
          | ]
          |}
          |""".stripMargin
      )
    }
  }
}
