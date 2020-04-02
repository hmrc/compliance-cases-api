package definition

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.WSClient
import play.api.test.Helpers.OK
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}

class DefinitionControllerISpec extends PlaySpec with GuiceOneServerPerSuite with FutureAwaits with DefaultAwaitTimeout {

  def wsClient: WSClient = app.injector.instanceOf[WSClient]

  "api/definition" should {
    "return the correct definition from config" in {
      val response = await(wsClient.url(s"http://localhost:$port/api/definition").get())

      response.status mustBe OK
      response.body[JsValue] mustBe Json.parse(
        s"""
           |{
           |  "scopes": [
           |    {
           |      "key": "write:compliance-cases-api",
           |      "name": "Create Cases",
           |      "description": "Scope to create cases in CaseFlow"
           |    }
           |  ],
           |  "api": {
           |    "name": "Compliance Cases",
           |    "description": "Api to manage compliance cases in CaseFlow",
           |    "context": "misc/compliance-cases",
           |    "categories": ["OTHER"],
           |    "versions": [
           |      {
           |        "version": "1.0",
           |        "status": "ALPHA",
           |        "endpointsEnabled": false,
           |        "access" : {
           |          "type": "PRIVATE",
           |          "whitelistedApplicationIds": []
           |        }
           |      }
           |    ]
           |  }
           |}
      """.stripMargin)
    }
  }
}