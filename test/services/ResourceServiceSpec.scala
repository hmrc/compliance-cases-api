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

package services

import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.{MustMatchers, WordSpec}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Environment
import play.api.libs.json.Json
import uk.gov.hmrc.http.HeaderCarrier

class ResourceServiceSpec extends WordSpec with MustMatchers with GuiceOneAppPerSuite with MockitoSugar
  with ScalaFutures with IntegrationPatience {

  private val env = Environment.simple()

  implicit lazy val hc: HeaderCarrier = HeaderCarrier(sessionId = None)

  val service = new ResourceService(env)



  "Service" should {

    "return exception" in {
      try {
        service.getJson("/schemas/1.json")
      } catch {
        case e: Exception =>
          e mustBe an[Exception]
          e.getLocalizedMessage mustBe "resource not found: /schemas/1.json"
      }
    }

    "return json" in {
      service.getJson("/schemas/caseflowCreateCaseSchema.json") mustBe Json.parse(models.ExamplePayloads.schema)
    }

    "return file" in {
      service.getFile("/schemas/caseflowCreateCaseSchema.json") mustBe models.ExamplePayloads.schema

    }
  }
}
