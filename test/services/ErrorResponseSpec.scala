/*
 * Copyright 2024 HM Revenue & Customs
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

import models.ErrorResponse
import models.Error
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers.mustBe
import play.api.http.Status.SERVICE_UNAVAILABLE
import play.api.libs.json.{JsObject, JsValue, Json}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}

class ErrorResponseSpec extends AnyWordSpec
  with FutureAwaits with DefaultAwaitTimeout
  with Matchers {

  val err = new Error("", "", None)
  val err1 = new Error("", "", None)
  
  val errorsList = List(err, err1)
  val errorModel: JsObject = Json.obj(
    "code" -> "SERVER_ERROR",
    "reason" -> "Service is unavailable",
    "path" -> None
  )

  val errorResponseModel: JsObject = Json.obj(
    "caseType" -> "error response",
    "errors" -> errorsList
  )

  "The Error" should {
    "parse to Json for Error" in {
      val underTest = Error("SERVER_ERROR", "Service is unavailable", None)
      underTest eq Some(errorModel)
    }

    "parse to Json for ErrorResponse" in {
      val underTest = ErrorResponse("error response", errorsList)
      underTest eq Some(errorResponseModel)
    }
  }
}

