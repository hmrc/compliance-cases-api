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

package definition

import helpers.WireMockSpec
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.JsonBodyReadables.readableAsJson
import play.api.libs.ws.WSClient
import play.api.test.Helpers.{NO_CONTENT, OK}
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}

class DefinitionControllerISpec extends PlaySpec with GuiceOneServerPerSuite with FutureAwaits with DefaultAwaitTimeout with WireMockSpec {

  def wsClient: WSClient = app.injector.instanceOf[WSClient]

  "api/definition" should {
    "return the correct definition from config" in {
      stubPostWithoutRequestAndResponseBody("/write/audit", NO_CONTENT)

      val response = await(buildClient(s"/api/definition").get())

      response.status mustBe OK
      response.body[JsValue] mustBe Json.parse(
        s"""
           |{
           |  "api": {
           |    "name": "Compliance Cases",
           |    "description": "Api to manage compliance cases in CaseFlow",
           |    "context": "misc/compliance-cases",
           |    "categories": ["PRIVATE_GOVERNMENT"],
           |    "versions": [
           |      {
           |        "version": "1.0",
           |        "status": "ALPHA",
           |        "endpointsEnabled": false,
           |        "access": {
           |          "type": "PRIVATE"
           |        }
           |      }
           |    ]
           |  }
           |}
      """.stripMargin)
    }
  }
}
