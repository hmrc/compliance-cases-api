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

import caseData.ComplianceCasesExamples._
import connectors.ComplianceCasesConnector
import org.mockito.Matchers
import org.mockito.Mockito.when
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.{MustMatchers, WordSpec}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.Status._
import play.api.libs.json.Json
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ComplianceCasesServiceSpec extends WordSpec with MustMatchers with GuiceOneAppPerSuite with MockitoSugar
  with ScalaFutures with IntegrationPatience {

  private val complianceCasesConnector = mock[ComplianceCasesConnector]
  private val service: ComplianceCasesService = new ComplianceCasesService(complianceCasesConnector)

  implicit lazy val hc: HeaderCarrier = HeaderCarrier(sessionId = None)

  "Service" should {

    "return 204" in {

      when(complianceCasesConnector.complianceInvestigations(Matchers.any())(Matchers.any(),Matchers.any()))
        .thenReturn(Future.successful(HttpResponse(NO_CONTENT)))

      val response = whenReady(service.complianceInvestigations(Json.parse(fullCaseJson))) { response => response }

      response.status mustBe NO_CONTENT
    }
  }
}
