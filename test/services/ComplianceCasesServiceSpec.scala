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

package services

import caseData.ComplianceCasesExamples._
import helpers.MockHelpers
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import play.api.http.Status._
import play.api.libs.json.Json
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}

import scala.concurrent.ExecutionContext.Implicits.global

class ComplianceCasesServiceSpec extends AnyWordSpecLike with Matchers with ScalaFutures with IntegrationPatience with MockHelpers {

  private val service: ComplianceCasesService = new ComplianceCasesService(mockComplianceCasesConnector)

  implicit lazy val hc: HeaderCarrier = HeaderCarrier()

  "Service" should {

    "return 204 on successful response from connector" in {
      Given
        .the.complianceCasesConnector.createsCase(
        Json.parse(fullCaseJson),
        "some-correlation-id",
        Some(HttpResponse(NO_CONTENT, ""))
      ).build()

      whenReady(service.createCase(Json.parse(fullCaseJson), "some-correlation-id")) {
        _.get.status shouldBe  NO_CONTENT
      }
    }
  }
}
