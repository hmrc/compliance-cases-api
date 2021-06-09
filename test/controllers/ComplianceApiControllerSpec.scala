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

package controllers

import java.util.UUID

import caseData.ComplianceCasesExamples._
import controllers.actions.{RequestWithCorrelationId, ValidateCorrelationIdHeaderAction}
import helpers.MockHelpers
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}
import play.api.http.Status
import play.api.libs.json.{JsNull, Json}
import play.api.mvc._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.http.HttpResponse

import scala.concurrent.{ExecutionContext, Future}


class ComplianceApiControllerSpec extends WordSpec with Matchers with MockFactory with MockHelpers {

  class Setup {
    val correlationId: String = UUID.randomUUID().toString

    object StubbedCorrelationIdAction extends ValidateCorrelationIdHeaderAction(
      new BodyParsers.Default(stubControllerComponents().parsers)
    )(
      stubControllerComponents().executionContext
    ) {
      override def invokeBlock[A](request: Request[A], block: RequestWithCorrelationId[A] => Future[Result]): Future[Result] = {
        block(RequestWithCorrelationId(request, correlationId))
      }
    }

    (mockAuthApplicationAction.andThen[Request](_:  ValidateCorrelationIdHeaderAction)) expects * once() returns StubbedCorrelationIdAction

    implicit val executionContext: ExecutionContext = stubControllerComponents().executionContext

    val controller = new ComplianceApiController(
      mockValidationService, mockResourceService, mockComplianceCasesService, StubbedCorrelationIdAction, mockAuthApplicationAction, stubControllerComponents()
    )
  }

  "The Compliance Api Controller" when {
    "serving Investigations api" ignore {
      "return Accepted for valid input" in new Setup {

        Given
          .the.complianceCasesService.createsCase(
            Json.parse(minimumRepaymentOrganisationJson),
            correlationId,
            Some(HttpResponse(ACCEPTED,exampleJsonSuccessResponse))
          )
          .the.resourceService.returnsResourceAt("/schemas/caseflowCreateCaseSchema.json","mySchema")
          .and.the.validationService.validate("mySchema", Json.parse(minimumRepaymentOrganisationJson), None)
          .build()

        val requestWithBody: FakeRequest[AnyContentAsJson] =
          FakeRequest("POST", "/case").withJsonBody(Json.parse(minimumRepaymentOrganisationJson))

        val result: Future[Result] = controller.createCase()(requestWithBody)

        status(result) shouldBe Status.ACCEPTED
        contentAsJson(result) shouldBe Json.parse(exampleJsonSuccessResponse)
      }

      "return InternalServerError if an None is returned from the service" in new Setup {

        Given
          .the.complianceCasesService.createsCase(
            Json.parse(addressJson),
            correlationId,
            None
          )
          .and.the.resourceService.returnsResourceAt("/schemas/caseflowCreateCaseSchema.json","oops None from service")
          .and.the.validationService.validate("oops None from service", Json.parse(addressJson), None)
          .build()

        val requestWithBody: FakeRequest[AnyContentAsJson] =
          FakeRequest("POST", "/case").withJsonBody(Json.parse(addressJson))

        val result: Future[Result] = controller.createCase()(requestWithBody)

        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        contentAsJson(result) shouldBe Json.obj(
          "code" -> "INTERNAL_SERVER_ERROR",
          "message" -> "Internal server error")
      }

      "return BadRequest for invalid input (validation error in controller)" in new Setup {
        Given
          .the.resourceService.returnsResourceAt("/schemas/caseflowCreateCaseSchema.json","errorValidation")
          .and.the.validationService.validate("errorValidation", Json.parse(addressJson), Some(Json.obj("this is an error" -> "my bad")))
          .build()

        val requestWithBody: FakeRequest[AnyContentAsJson] =
          FakeRequest("POST", "/case").withJsonBody(Json.parse(addressJson))

        val result: Future[Result] = controller.createCase()(requestWithBody)

        status(result) shouldBe Status.BAD_REQUEST
        contentAsJson(result) shouldBe Json.obj("this is an error" -> "my bad")
      }

      "return BadRequest for invalid input (validation error in controller) and convert an Empty body to JsNull" in new Setup {
        Given
          .the.resourceService.returnsResourceAt("/schemas/caseflowCreateCaseSchema.json","errorValidation")
          .and.the.validationService.validate("errorValidation", JsNull, Some(Json.obj("this is an error" -> "my bad")))
          .build()

        val requestWithBody: FakeRequest[AnyContent] =
          FakeRequest("POST", "/case")

        val result: Future[Result] = controller.createCase()(requestWithBody)

        status(result) shouldBe Status.BAD_REQUEST
        contentAsJson(result) shouldBe Json.obj("this is an error" -> "my bad")
      }
    }
  }
}

