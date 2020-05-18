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

import akka.stream.Materializer
import caseData.ComplianceCasesExamples._
import controllers.actions.RequestWithCorrelationId
import org.mockito.ArgumentMatchersSugar
import org.mockito.Mockito.{reset, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.{JsString, Json}
import play.api.test.FakeRequest
import uk.gov.hmrc.http.HeaderCarrier

class ValidationServiceSpec extends PlaySpec with GuiceOneAppPerSuite with MockitoSugar
  with ArgumentMatchersSugar with ScalaFutures with IntegrationPatience with BeforeAndAfterEach {

  import play.api.mvc._

  implicit lazy val hc: HeaderCarrier = HeaderCarrier(sessionId = None)

  implicit lazy val materializer: Materializer = app.materializer
  val bodyParser = new BodyParsers.Default
  val mockResource: ResourceService = mock[ResourceService]
  implicit val request: RequestWithCorrelationId[AnyContentAsEmpty.type] = RequestWithCorrelationId(FakeRequest(), "Some-Correlation-Id")

  def validationService = new ValidationService(mockResource)

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockResource)
  }

  "validationService" should {

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

    "return json schema errors" in {
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
          |   {"code":"INVALID_FIELD","message":"Invalid value in field","path":"/case"},
          |   {"code":"INVALID_FIELD","message":"Invalid value in field","path":"/sourceSystemKey"}
          | ]
          |}
          |""".stripMargin)
    }

    "return invalid fields in risk" in {
      when(mockResource.getFile(eqTo("/schemas/caseflowCreateRiskCaseSchema.json"))).thenReturn(caseflowCreateRiskCaseSchema)
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
      when(mockResource.getFile(eqTo("/schemas/caseflowCreateRepaymentCaseSchema.json"))).thenReturn(caseflowCreateRepaymentCaseSchema)
      validationService.validateAndRetrieveErrors(caseflowCreateCaseSchema,
        Json.parse(invalidRepaymentCaseJson)).get mustBe Json.parse(
        """
          |{
          | "code": "INVALID_PAYLOAD",
          | "message": "Submission has not passed validation for the Repayment model. Invalid payload.",
          | "errors":
          | [
          |   {"code":"UNEXPECTED_FIELD","message":"Unexpected field found","path":"/case/whoami"},
          |   {"code":"MISSING_FIELD","message":"Expected field not present","path":"/case/taxPeriodStart"},
          |   {"code":"MISSING_FIELD","message":"Expected field not present","path":"/case/taxPayer/referenceNumbers/0/referenceType"},
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
