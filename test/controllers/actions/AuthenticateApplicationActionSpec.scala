/*
 * Copyright 2023 HM Revenue & Customs
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

package controllers.actions

import helpers.MockHelpers
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.slf4j.MDC
import play.api.libs.json.Json
import play.api.mvc.Results.Ok
import play.api.mvc.{BodyParsers, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.auth.core.AuthProvider.StandardApplication
import uk.gov.hmrc.auth.core.{AuthProviders, BearerTokenExpired}
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.http.{HeaderCarrier, HeaderNames, RequestId, SessionId}

import scala.concurrent.{ExecutionContext, Future}

class AuthenticateApplicationActionSpec extends AnyWordSpecLike with Matchers with MockHelpers {

  class Setup {
    val mockBodyParser: BodyParsers.Default = new BodyParsers.Default(stubControllerComponents().parsers)
    implicit val ec: ExecutionContext = stubControllerComponents().executionContext

    lazy val action: AuthenticateApplicationAction = new AuthenticateApplicationAction(mockAuthConnector, mockBodyParser)


    def mockBody: Future[Result] = Future.successful(Ok("{}"))
  }

  "updateContextWithRequestId" should {
    "update context with request id if MDC is empty and only request id is in the header carrier" in new Setup {
      MDC.clear()
      implicit val hc: HeaderCarrier = HeaderCarrier(requestId = Some(RequestId("one-two-three")))
      action.updateContextWithRequestId
      MDC.get(HeaderNames.xRequestId) shouldBe "one-two-three"
      Option(MDC.get(HeaderNames.xSessionId)) shouldBe None
    }
    "update context with request and session ids if MDC is empty  and " in new Setup {
      MDC.clear()
      implicit val hc: HeaderCarrier = HeaderCarrier(requestId = Some(RequestId("one-two-three")), sessionId = Some(SessionId("one-two-four")))
      action.updateContextWithRequestId
      MDC.get(HeaderNames.xRequestId) shouldBe "one-two-three"
      MDC.get(HeaderNames.xSessionId) shouldBe "one-two-four"
    }
    "do not update context if it exists" in new Setup {
      MDC.clear()
      MDC.put(HeaderNames.xRequestId, "silly")
      implicit val hc: HeaderCarrier = HeaderCarrier(requestId = Some(RequestId("one-two-three")))
      action.updateContextWithRequestId
      MDC.get(HeaderNames.xRequestId) shouldBe "silly"
    }
    "do not update context if it exists with no request id" in new Setup {
      MDC.clear()
      MDC.put("weee", "silly")
      implicit val hc: HeaderCarrier = HeaderCarrier(requestId = Some(RequestId("one-two-three")))
      action.updateContextWithRequestId
      Option(MDC.get(HeaderNames.xRequestId)) shouldBe None
    }
  }

  "action.async" should {
    s"return a $UNAUTHORIZED if not authorised by auth" in new Setup {
      Given
        .the.authConnector.authenticatesWithResult(AuthProviders(StandardApplication), Retrievals.applicationId, Future.failed(BearerTokenExpired("an exception has occurred")))
        .build()

      val result: Future[Result] = action.async(mockBody)(FakeRequest())

      status(result) shouldBe UNAUTHORIZED
      contentAsJson(result) shouldBe Json.obj("code" -> "UNAUTHORIZED", "message" -> "Bearer token is missing or not authorized")
    }
    s"return a $UNAUTHORIZED if no application id is present" in new Setup {
      Given
        .the.authConnector.authenticatesWithResult(AuthProviders(StandardApplication), Retrievals.applicationId, Future.successful(None))
        .build()

      val result: Future[Result] = action.async(mockBody)(FakeRequest())

      status(result) shouldBe UNAUTHORIZED
      contentAsJson(result) shouldBe Json.obj("code" -> "UNAUTHORIZED", "message" -> "Bearer token is missing or not authorized")
    }

    s"return a $INTERNAL_SERVER_ERROR if an unexpected exception occurs" in new Setup {
      Given
        .the.authConnector.authenticatesWithResult(AuthProviders(StandardApplication), Retrievals.applicationId,Future.failed(new NullPointerException("error")))
        .build()

      val result: Future[Result] = action.async(mockBody)(FakeRequest())

      status(result) shouldBe INTERNAL_SERVER_ERROR
      contentAsJson(result) shouldBe Json.obj("code" -> "INTERNAL_SERVER_ERROR", "message" -> "Internal server error")
    }
  }
}
