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

import akka.stream.Materializer
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.{MustMatchers, WordSpec}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.Json
import uk.gov.hmrc.http.HeaderCarrier
import caseData.ComplianceCasesExamples._

class ValidationServiceSpec extends WordSpec with MustMatchers with GuiceOneAppPerSuite with MockitoSugar
  with ScalaFutures with IntegrationPatience {

  import play.api.mvc._

  import scala.concurrent.ExecutionContext.Implicits.global

  implicit lazy val hc: HeaderCarrier = HeaderCarrier(sessionId = None)

  implicit lazy val materializer: Materializer = app.materializer
  val bodyParser = new BodyParsers.Default
  val validationService = new ValidationService(bodyParser)

  "validationService" should {
    "return json errors when schema validation passes but model does not map" in {
      validationService.validate(testSchema, Json.parse(testJson2)).left.get mustBe Json.parse(
        """
          |{
          |   "mappingErrors":
          |     [
          |       "/Case/CampaignID - error.expected.jsstring"
          |     ]
          |}
          |""".stripMargin)
    }

    "return json schema errors" in {
      validationService.validate(schema, Json.parse("""{"Case":[]}""")).left.get mustBe Json.parse(
        """
          |{
          |   "errors":
          |     [
          |       "instance type (array) does not match any allowed primitive type (allowed: [\"object\"])"
          |     ]
          |}
          |""".stripMargin)
    }
  }
}
