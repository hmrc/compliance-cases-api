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

package controllers

import java.util.UUID

import akka.stream.Materializer
import caseData.ComplianceCasesExamples._
import controllers.actions.{AuthenticateApplicationAction, ValidateCorrelationIdHeaderAction}
import org.mockito.ArgumentMatchersSugar
import org.mockito.Mockito.when
import org.mockito.invocation.InvocationOnMock
import org.scalatest.{Matchers, WordSpec}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.http.Status
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Request
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.ComplianceCasesService
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}

import scala.concurrent.{ExecutionContext, Future}


class ComplianceApiControllerSpec extends WordSpec with Matchers with MockitoSugar with ArgumentMatchersSugar with GuiceOneAppPerSuite {
  override lazy val app: Application = {
    import play.api.inject._

    new GuiceApplicationBuilder()
      .overrides(
        bind[ComplianceCasesService].toInstance(service),
        bind[AuthenticateApplicationAction].toInstance(mockAuthApp)
      ).build()
  }

  lazy val mockAuthApp: AuthenticateApplicationAction = mock[AuthenticateApplicationAction]
  val correlationId: String = UUID.randomUUID().toString

  implicit lazy val materializer: Materializer = app.materializer
  private lazy val service: ComplianceCasesService = mock[ComplianceCasesService]

  when(mockAuthApp.andThen[Request](any[AuthenticateApplicationAction])).thenAnswer(
    (invocation: InvocationOnMock) => invocation.getArguments()(0).asInstanceOf[ValidateCorrelationIdHeaderAction]
  )


  "The Compliance Api Controller" when {
    "serving Investigations api" should {
      "return Accepted for valid input" in {
        when(service.createCase(any[JsValue], eqTo(correlationId))(any[HeaderCarrier], any[ExecutionContext]))
          .thenReturn(Future.successful(Some(HttpResponse(ACCEPTED, Some(Json.parse(exampleJsonSuccessResponse)),
            Map("Content-Type" -> Seq("application/json"), "header" -> Seq("`123")))
          )))

        route(app, FakeRequest(POST, routes.ComplianceApiController.createCase().url)
          .withHeaders("CorrelationId" -> correlationId)
          .withJsonBody(Json.parse(minimumRepaymentOrganisationJson)))
          .map {
            result => {
              status(result) shouldBe Status.ACCEPTED
              contentAsJson(result) shouldBe Json.parse(exampleJsonSuccessResponse)
            }
          }
      }

      "return Accepted for valid full individual repayment input" in {
        when(service.createCase(any[JsValue], eqTo(correlationId))(any[HeaderCarrier], any[ExecutionContext]))
          .thenReturn(Future.successful(Some(HttpResponse(ACCEPTED, Some(Json.parse(exampleJsonSuccessResponse))))))

        route(app, FakeRequest(POST, routes.ComplianceApiController.createCase().url)
          .withHeaders("CorrelationId" -> correlationId)
          .withJsonBody(Json.parse(fullCaseJson)))
          .map {
            result => {
              status(result) shouldBe Status.ACCEPTED
              contentAsJson(result) shouldBe Json.parse(exampleJsonSuccessResponse)
            }
          }
      }

      "return Accepted for valid risk input" in {
        when(service.createCase(any[JsValue], eqTo(correlationId))(any[HeaderCarrier], any[ExecutionContext]))
          .thenReturn(Future.successful(Some(HttpResponse(ACCEPTED, Some(Json.parse(exampleJsonSuccessResponse)),
            Map("Content-Type" -> Seq("application/json"), "header" -> Seq("`123")))
          )))

        route(app, FakeRequest(POST, routes.ComplianceApiController.createCase().url)
          .withHeaders("CorrelationId" -> correlationId)
          .withJsonBody(Json.parse(minimumRiskJson)))
          .map {
            result => {
              status(result) shouldBe Status.ACCEPTED
              contentAsJson(result) shouldBe Json.parse(exampleJsonSuccessResponse)
            }
          }
      }

      "return Accepted for valid input with address" in {
        when(service.createCase(any[JsValue], eqTo(correlationId))(any[HeaderCarrier], any[ExecutionContext]))
          .thenReturn(Future.successful(Some(HttpResponse(ACCEPTED, Some(Json.parse(exampleJsonSuccessResponse))))))

        route(app, FakeRequest(POST, routes.ComplianceApiController.createCase().url)
          .withHeaders("CorrelationId" -> correlationId)
          .withJsonBody(Json.parse(addressJson)))
          .map {
            result => status(result) shouldBe Status.ACCEPTED
          }
      }

      "return InternalServerError if an None is returned from the service" in {
        when(service.createCase(any[JsValue], eqTo(correlationId))(any[HeaderCarrier], any[ExecutionContext]))
          .thenReturn(Future.successful(None))

        route(app, FakeRequest(POST, routes.ComplianceApiController.createCase().url)
          .withHeaders("CorrelationId" -> correlationId)
          .withJsonBody(Json.parse(addressJson)))
          .map {
            result =>
              status(result) shouldBe Status.INTERNAL_SERVER_ERROR
              contentAsJson(result) shouldBe Json.obj(
                "code" -> "INTERNAL_SERVER_ERROR",
                "message" -> "Internal server error")
          }
      }

      "return BadRequest for invalid input (validation error in controller)" in {
        route(app, FakeRequest(POST, routes.ComplianceApiController.createCase().url)
          .withHeaders("CorrelationId" -> correlationId)
          .withJsonBody(Json.parse(incorrectJson)))
          .map {
            result => status(result) shouldBe Status.BAD_REQUEST
          }
      }

      "return BadRequest for no json" in {
        route(app, FakeRequest(POST, routes.ComplianceApiController.createCase().url)
          .withHeaders("CorrelationId" -> correlationId))
          .map {
            result => status(result) shouldBe Status.BAD_REQUEST
          }
      }

      "return BadRequest for valid input (error passed back from connector)" in {
        when(service.createCase(any[JsValue], eqTo(correlationId))(any[HeaderCarrier], any[ExecutionContext]))
          .thenReturn(Future.successful(
            Some(HttpResponse(BAD_REQUEST, Some(Json.parse(exampleJsonErrorResponse))))
          ))

        route(app, FakeRequest(POST, routes.ComplianceApiController.createCase().url)
          .withHeaders("CorrelationId" -> correlationId)
          .withJsonBody(Json.parse(minimumRepaymentOrganisationJson)))
          .map {
            result => {
              status(result) shouldBe Status.BAD_REQUEST
              contentAsJson(result) shouldBe Json.parse(exampleJsonErrorResponse)
            }
          }
      }
      "return BadRequest if no correlationId is present" in {
        route(app, FakeRequest(POST, routes.ComplianceApiController.createCase().url)
          .withJsonBody(Json.parse(minimumRepaymentOrganisationJson)))
          .map {
            result => {
              status(result) shouldBe Status.BAD_REQUEST
              contentAsJson(result) shouldBe Json.obj(
                "code" -> "MISSING_CORRELATION_ID",
                "message" -> "Submission has not passed validation. Missing header CorrelationId."
              )
            }
          }
      }
      "return BadRequest if no correlationId is invalid" in {
        route(app, FakeRequest(POST, routes.ComplianceApiController.createCase().url)
          .withHeaders("CorrelationId" -> "some-correlation-id")
          .withJsonBody(Json.parse(minimumRepaymentOrganisationJson)))
          .map {
            result => {
              status(result) shouldBe Status.BAD_REQUEST
              contentAsJson(result) shouldBe Json.obj(
                "code" -> "INVALID_CORRELATION_ID",
                "message" -> "Submission has not passed validation. Invalid header CorrelationId."
              )
            }
          }
      }
    }
  }
}

