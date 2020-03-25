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
import models.ComplianceInvestigations
import org.mockito.Matchers.{any, eq => eqTo}
import org.mockito.Mockito.{reset, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.Json
import uk.gov.hmrc.http.HeaderCarrier

class ValidationServiceSpec extends PlaySpec with GuiceOneAppPerSuite with MockitoSugar
  with ScalaFutures with IntegrationPatience with BeforeAndAfterEach {

  import play.api.mvc._

  import scala.concurrent.ExecutionContext.Implicits.global

  implicit lazy val hc: HeaderCarrier = HeaderCarrier(sessionId = None)

  implicit lazy val materializer: Materializer = app.materializer
  val bodyParser = new BodyParsers.Default
  val mockResource = mock[ResourceService]

  def validationService = new ValidationService(bodyParser, mockResource)

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockResource)
  }

  "validationService" should {
    "return json errors when schema validation passes but model does not map" in {
      when(mockResource.getFile(any())).thenReturn("{}")
      validationService.validate[ComplianceInvestigations]("{}", Json.parse(requestJsonWithoutSourceSystemId)).left.get mustBe Json.parse(
        """
          |{
          |"code": "JSON_VALIDATION_ERROR",
          |"message": "The provided JSON was unable to be validated",
          |"errors":
          |[
          |  {"code":"BAD_REQUEST","message":"an invalid value provided","path":"/sourceSystemId"}
          |]
          |}
          |""".stripMargin)
    }

    "return json schema errors" in {
      validationService.validate[ComplianceInvestigations](caseflowCreateCaseSchema,
        Json.parse("""{"sourceSystemId": "CNT", "sourceSystemKey": [], "case": []}""")).left.get mustBe Json.parse(
        """
          |{
          |"code": "JSON_VALIDATION_ERROR",
          |"message": "The provided JSON was unable to be validated",
          |"errors":
          |[
          | {"code":"BAD_REQUEST","message":"an invalid value provided","path":"/case"},
          | {"code":"BAD_REQUEST","message":"an invalid value provided","path":"/sourceSystemKey"}
          |]
          |}
          |""".stripMargin)
    }

    "return invalid fields in risk" in {
      when(mockResource.getFile(eqTo("/schemas/caseflowCreateRiskCaseSchema.json"))).thenReturn(caseflowCreateRiskCaseSchema)
      validationService.validate[ComplianceInvestigations](caseflowCreateCaseSchema,
        Json.parse(invalidRiskCaseJson)).left.get mustBe Json.parse(
        """
          |{
          |"code": "JSON_VALIDATION_ERROR",
          |"message": "The provided JSON was unable to be validated as the Risk model.",
          |"errors":
          |[
          |{"code":"BAD_REQUEST","message":"an invalid value provided","path":"/case/sourceSystemRef"}
          |]
          |}
          |""".stripMargin
      )
    }

    "return missing and invalid fields in repayments" in {
      when(mockResource.getFile(eqTo("/schemas/caseflowCreateRepaymentCaseSchema.json"))).thenReturn(caseflowCreateRepaymentCaseSchema)
      validationService.validate[ComplianceInvestigations](caseflowCreateCaseSchema,
        Json.parse(invalidRepaymentCaseJson)).left.get mustBe Json.parse(
        """
          |{
          |"code": "JSON_VALIDATION_ERROR",
          |"message": "The provided JSON was unable to be validated as the Repayment model.",
          |"errors":
          |[
          |{"code":"MISSING_FIELD","message":"field not present","path":"/case/taxPeriodStart"},
          |{"code":"MISSING_FIELD","message":"field not present","path":"/case/taxPayer/referenceNumber/referenceType"},
          |{"code":"BAD_REQUEST","message":"an invalid value provided","path":"/case/taxRegime"}
          |]
          |}
          |""".stripMargin
      )
    }

    "return invalid caseType using incorrect datatype" in {
      validationService.validate[ComplianceInvestigations](caseflowCreateCaseSchema,
        Json.parse(invalidCaseTypeUsingIncorrectDatatypeJson)).left.get mustBe Json.parse(
        """
          |{
          |"code": "JSON_VALIDATION_ERROR",
          |"message": "The provided JSON was unable to be validated",
          |"errors":
          |[
          |{"code":"BAD_REQUEST","message":"an invalid value provided","path":""}
          |]
          |}
          |""".stripMargin
      )
    }

    "return invalid caseType" in {
      validationService.validate[ComplianceInvestigations](caseflowCreateCaseSchema,
        Json.parse(invalidCaseType)).left.get mustBe Json.parse(
        """
          |{
          |"code": "JSON_VALIDATION_ERROR",
          |"message": "The provided JSON was unable to be validated",
          |"errors":
          |[
          |{"code":"BAD_REQUEST","message":"an invalid value provided","path":"/case/caseType"}
          |]
          |}
          |""".stripMargin
      )
    }
  }
}


