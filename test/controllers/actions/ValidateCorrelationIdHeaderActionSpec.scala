/*
 * Copyright 2022 HM Revenue & Customs
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

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.util.UUID
import play.api.libs.json.Json
import play.api.mvc.Results.Ok
import play.api.mvc.{BodyParsers, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsJson, status, stubControllerComponents, _}

import scala.concurrent.{ExecutionContext, Future}

class ValidateCorrelationIdHeaderActionSpec extends AnyWordSpecLike with Matchers {

  class Setup {
    val mockBodyParser: BodyParsers.Default = new BodyParsers.Default(stubControllerComponents().parsers)

    implicit val ec: ExecutionContext = stubControllerComponents().executionContext

    val action: ValidateCorrelationIdHeaderAction = new ValidateCorrelationIdHeaderAction(mockBodyParser)

    def mockBody: Future[Result] = Future.successful(Ok("{}"))
  }

  "action.async" must {
    "return BadRequest if no correlationId is present" in new Setup {

      val result: Future[Result] = action.async(mockBody)(FakeRequest())

      status(result) shouldBe BAD_REQUEST
      contentAsJson(result) shouldBe Json.obj(
        "code" -> "MISSING_CORRELATION_ID",
        "message" -> "Submission has not passed validation. Missing header CorrelationId."
      )
    }
    "return BadRequest if correlationId is in valid" in new Setup {
      val result: Future[Result] = action.async(mockBody)(FakeRequest().withHeaders("CorrelationId" -> "12345"))

      status(result) shouldBe BAD_REQUEST
      contentAsJson(result) shouldBe Json.obj(
        "code" -> "INVALID_CORRELATION_ID",
        "message" -> "Submission has not passed validation. Invalid header CorrelationId."
      )
    }
    "return Ok if correlationId is valid" in new Setup {
      val result: Future[Result] = action.async(mockBody)(FakeRequest().withHeaders("CorrelationId" -> UUID.randomUUID().toString))

      status(result) shouldBe OK
      contentAsJson(result) shouldBe Json.obj()
    }
  }
}
