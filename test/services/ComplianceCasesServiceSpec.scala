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

import org.scalatest.{MustMatchers, WordSpec}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.FakeRequest
import play.api.{Configuration, Environment, _}
import uk.gov.hmrc.play.bootstrap.config.{RunMode, ServicesConfig}
import config.AppConfig
import connectors.ComplianceCasesConnector
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Matchers
import org.mockito.Mockito.when
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import play.api.libs.json.Json
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import caseData.ComplianceCasesExamples._

import scala.concurrent.Future

class ComplianceCasesServiceSpec extends WordSpec with MustMatchers with GuiceOneAppPerSuite with MockitoSugar
  with ScalaFutures with IntegrationPatience {

  import scala.concurrent.ExecutionContext.Implicits.global
  import play.api.http.Status._

  private val fakeRequest = FakeRequest("GET", "/")
  private val env           = Environment.simple()
  private val configuration = Configuration.load(env)
  private val serviceConfig = new ServicesConfig(configuration, new RunMode(configuration, Mode.Dev))
  private val appConfig     = new AppConfig(configuration, serviceConfig)
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
