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
import org.scalatest.funsuite.AnyFunSuite
import play.api.libs.json.{JsSuccess, JsError, Json}

class ErrorResponseSpec extends AnyFunSuite {

  test("should serialize Error Class to correct JSON ") {
    val error = Error("404", "Not Found", Some("/path"))
    val json = Json.toJson(error)

    val expectedJson = Json.parse("""{"code":"404","message":"Not Found","path":"/path"}""")
    assert(json == expectedJson)
  }

  test("should deserialize Error Class to correct JSON ") {
    val json = Json.parse("""{"code":"404","message":"Not Found","path":"/path"}""")
    val result = json.validate[Error]

    assert(result == JsSuccess(Error("404", "Not Found", Some("/path"))))
  }

  test("should serialize ErrorResponse Class to correct JSON ") {
    val errorResponse = ErrorResponse("Risk", List(Error("404", "Not Found", Some("/path"))))
    val json = Json.toJson(errorResponse)

    val expectedJson = Json.parse("""{"caseType":"Risk","errors":[{"code":"404","message":"Not Found","path":"/path"}]}""")
    assert(json == expectedJson)
  }

  test("should deserialize ErrorResponse Class to correct JSON ") {
    val json = Json.parse("""{"caseType":"Risk","errors":[{"code":"404","message":"Not Found","path":"/path"}]}""")

    val result = json.validate[ErrorResponse]
    assert(result == JsSuccess(ErrorResponse("Risk", List(Error("404", "Not Found", Some("/path"))))))
  }

}

