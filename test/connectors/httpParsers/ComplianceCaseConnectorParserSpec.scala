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

package connectors.httpParsers

import helpers.{LoggerHelper, MockHelpers}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import play.api.Configuration
import play.api.http.Status.{BAD_REQUEST, INTERNAL_SERVER_ERROR, NOT_FOUND, OK, UNPROCESSABLE_ENTITY}
import play.api.libs.json.{JsArray, JsObject, JsString, JsValue, Json}
import uk.gov.hmrc.http.HttpResponse

class ComplianceCaseConnectorParserSpec extends AnyWordSpecLike with Matchers with MockHelpers with LoggerHelper {

  private val correlationId: String = "some-correlation-id"
  private val caseType = "caseType"

  private val url = "some_url"

  private val errorMessage = "You messed up!!!"

  object connectorParser extends ComplianceCaseConnectorParser {
    override val className: String = "className"
    override val config: Configuration = Configuration(
      "errorMessages" -> Map(s"$caseType-001" -> errorMessage)
    )
  }

  private val httpRead = connectorParser.customHttpRead(correlationId, caseType)

  "customDesRead" should {
    "return IFResponse None for HttpResponse NOT_FOUND" in {
      val notFoundResponse = HttpResponse(NOT_FOUND, Json.toJson(""), Map.empty)
      val ifResponse = httpRead(url, notFoundResponse)
      ifResponse shouldBe None
    }

    "return IFResponse None for HttpResponse BAD_REQUEST" in {
      val notFoundResponse = HttpResponse(BAD_REQUEST, Json.toJson(""), Map.empty)
      val ifResponse = httpRead(url, notFoundResponse)
      ifResponse shouldBe None
    }

    "return IFResponse Some(HttpErrorResponse) for HttpResponse UNPROCESSABLE_ENTITY" in {
      val failureJson = Json.toJson(JsObject(Seq("failures" -> JsArray(Seq(JsObject(Seq("code" -> JsString("001"))))))))
      val unprocessableEntityResponse = HttpResponse(UNPROCESSABLE_ENTITY, failureJson, Map.empty)
      val ifResponse = httpRead(url, unprocessableEntityResponse)
      ifResponse.get.status shouldBe UNPROCESSABLE_ENTITY
      ifResponse.get.body.isEmpty shouldBe false
    }

    "return IFResponse None for HttpResponse != OK" in {
      val notFoundResponse = HttpResponse(INTERNAL_SERVER_ERROR, Json.toJson(""), Map.empty)
      val ifResponse = httpRead(url, notFoundResponse)
      ifResponse shouldBe None
    }

    "return IFResponse Some(response) for HttpResponse != OK" in {
      val okResponse = HttpResponse(OK, Json.toJson("{ \"value\": \"ok\"}"), Map.empty)
      val ifResponse = httpRead(url, okResponse)
      ifResponse.get shouldBe okResponse
    }
  }

}
