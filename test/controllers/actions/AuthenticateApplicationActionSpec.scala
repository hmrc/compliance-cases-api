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

package controllers.actions

import org.mockito.Matchers.{any, eq => eqTo}
import org.mockito.Mockito.when
import org.scalatest.{Matchers, WordSpec}
import org.scalatestplus.mockito.MockitoSugar
import play.api.libs.json.Json
import play.api.mvc.Results.Ok
import play.api.mvc.{BodyParsers, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.{ConfigLoader, Configuration}
import uk.gov.hmrc.auth.core.AuthProvider.StandardApplication
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.auth.core.{AuthProviders, BearerTokenExpired}
import uk.gov.hmrc.play.bootstrap.auth.DefaultAuthConnector

import scala.concurrent.{ExecutionContext, Future}

class AuthenticateApplicationActionSpec extends WordSpec with Matchers with MockitoSugar {

  class Setup {
    val mockAuthConnector: DefaultAuthConnector = mock[DefaultAuthConnector]
    val mockConfig: Configuration = mock[Configuration]
    val mockBodyParser: BodyParsers.Default = new BodyParsers.Default(stubControllerComponents().parsers)
    implicit val ec: ExecutionContext = ExecutionContext.global

    val action: AuthenticateApplicationAction = new AuthenticateApplicationAction(mockAuthConnector, mockConfig, mockBodyParser)

    def mockBody: Future[Result] = Future.successful(Ok("{}"))
  }

  "action.async" should {
    s"return a $OK if application id matches a whitelisted application id" in new Setup {
      when(mockAuthConnector.authorise(eqTo(AuthProviders(StandardApplication)), eqTo(Retrievals.applicationId))(any(), any()))
        .thenReturn(Future.successful(Some("ID-3")))

      when(mockConfig.get(eqTo("apiDefinition.whitelistedApplicationIds"))(any[ConfigLoader[Option[Seq[String]]]]()))
        .thenReturn(Some(Seq("ID-3", "ID-2")))

      val result: Future[Result] = action.async(mockBody)(FakeRequest())

      status(result) shouldBe OK
      contentAsJson(result) shouldBe Json.obj()
    }
    s"return a $UNAUTHORIZED if not authorised by auth" in new Setup {
      when(mockAuthConnector.authorise(eqTo(AuthProviders(StandardApplication)), eqTo(Retrievals.applicationId))(any(), any()))
        .thenReturn(Future.failed(BearerTokenExpired("an exception has occurred")))

      val result: Future[Result] = action.async(mockBody)(FakeRequest())

      status(result) shouldBe UNAUTHORIZED
      contentAsJson(result) shouldBe Json.obj("code" -> "UNAUTHORIZED", "message" -> "Bearer token is missing or not authorized")
    }
    s"return a $UNAUTHORIZED if no application id is present" in new Setup {
      when(mockAuthConnector.authorise(eqTo(AuthProviders(StandardApplication)), eqTo(Retrievals.applicationId))(any(), any()))
        .thenReturn(Future.successful(None))

      val result: Future[Result] = action.async(mockBody)(FakeRequest())

      status(result) shouldBe UNAUTHORIZED
      contentAsJson(result) shouldBe Json.obj("code" -> "UNAUTHORIZED", "message" -> "Bearer token is missing or not authorized")
    }
    s"return a $UNAUTHORIZED if application id doesn't match a whitelisted application id" in new Setup {
      when(mockAuthConnector.authorise(eqTo(AuthProviders(StandardApplication)), eqTo(Retrievals.applicationId))(any(), any()))
        .thenReturn(Future.successful(Some("ID-3")))

      when(mockConfig.get(eqTo("apiDefinition.whitelistedApplicationIds"))(any[ConfigLoader[Option[Seq[String]]]]()))
        .thenReturn(Some(Seq("ID-1", "ID-2")))

      val result: Future[Result] = action.async(mockBody)(FakeRequest())

      status(result) shouldBe UNAUTHORIZED
      contentAsJson(result) shouldBe Json.obj("code" -> "UNAUTHORIZED", "message" -> "Bearer token is missing or not authorized")
    }

    s"return a $INTERNAL_SERVER_ERROR if an unexpected exception occurs" in new Setup {
      when(mockAuthConnector.authorise(eqTo(AuthProviders(StandardApplication)), eqTo(Retrievals.applicationId))(any(), any()))
        .thenReturn(Future.failed(new NullPointerException("error")))

      val result: Future[Result] = action.async(mockBody)(FakeRequest())

      status(result) shouldBe INTERNAL_SERVER_ERROR
      contentAsJson(result) shouldBe Json.obj("code" -> "INTERNAL_SERVER_ERROR", "message" -> "Internal server error")
    }
  }
}
