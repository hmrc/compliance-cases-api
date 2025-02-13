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

package helpers

import connectors.ComplianceCasesConnector
import controllers.actions.AuthenticateApplicationAction
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.when
import org.mockito.stubbing.OngoingStubbing
import org.scalatestplus.mockito.MockitoSugar
import play.api.Configuration
import play.api.libs.json.JsValue
import services.{ComplianceCasesService, ResourceService, ValidationService}
import uk.gov.hmrc.auth.core.AuthConnector
import uk.gov.hmrc.auth.core.authorise.Predicate
import uk.gov.hmrc.auth.core.retrieve.Retrieval
import uk.gov.hmrc.http.HttpResponse

import scala.concurrent.Future

trait MockHelpers extends MockitoSugar {

  lazy val mockResourceService: ResourceService = mock[ResourceService]
  lazy val mockValidationService: ValidationService = mock[ValidationService]
  lazy val mockComplianceCasesService: ComplianceCasesService = mock[ComplianceCasesService]
  lazy val mockComplianceCasesConnector: ComplianceCasesConnector = mock[ComplianceCasesConnector]
  lazy val mockAuthConnector: AuthConnector = mock[AuthConnector]
  lazy val mockConfig: Configuration = mock[Configuration]
  lazy val mockAuthApplicationAction: AuthenticateApplicationAction = mock[AuthenticateApplicationAction]

  object Given extends MockPredicate()

  private[helpers] case class ConfigPredicate(private val stubs: Seq[() => OngoingStubbing[_]]) extends MockPredicate(stubs) {
    def getsConfigAt[A](address: String, configValue: A): ConfigPredicate = copy(
      stubs = stubs :+ (() => when(mockConfig.get[A](ArgumentMatchers.eq(address))(ArgumentMatchers.any())).thenReturn(configValue))
    )
  }

  private[helpers] case class ResourceServicePredicate(private val stubs: Seq[() => OngoingStubbing[_]]) extends MockPredicate(stubs) {
    def returnsResourceAt(address: String, resourceAsString: String): ResourceServicePredicate = copy(
      stubs = stubs :+ (() => when(mockResourceService.getFile(ArgumentMatchers.eq(address))).thenReturn(resourceAsString))
    )
  }

  private[helpers] case class ComplianceCasesServicePredicate(private val stubs: Seq[() => OngoingStubbing[_]]) extends MockPredicate(stubs) {
    def createsCase(input: JsValue, correlationId: String, httpResponse: Option[HttpResponse]): ComplianceCasesServicePredicate = copy(
      stubs = stubs :+ (() =>
        when(
          mockComplianceCasesService.createCase(
            ArgumentMatchers.eq(input),
            ArgumentMatchers.eq(correlationId)
          )(ArgumentMatchers.any(), ArgumentMatchers.any())
        ).thenReturn(Future.successful(httpResponse)))
    )
  }

  private[helpers] case class ComplianceCasesConnectorPredicate(private val stubs: Seq[() => OngoingStubbing[_]]) extends MockPredicate(stubs) {
    def createsCase(input: JsValue, correlationId: String, httpResponse: Option[HttpResponse]): ComplianceCasesConnectorPredicate = copy(
      stubs = stubs :+ (() => when(
        mockComplianceCasesConnector.createCase(
          ArgumentMatchers.eq(input),
          ArgumentMatchers.eq(correlationId)
        )(ArgumentMatchers.any(), ArgumentMatchers.any())
      ).thenReturn(Future.successful(httpResponse)))
    )
  }

  private[helpers] case class ValidationServicePredicate(private val stubs: Seq[() => OngoingStubbing[_]]) extends MockPredicate(stubs) {
    def validate(schema: String, json: JsValue, expectedOutcome: Option[JsValue]): ValidationServicePredicate = copy(
      stubs = stubs :+ (() => when(
        mockValidationService.validateAndRetrieveErrors(
          ArgumentMatchers.eq(schema),
          ArgumentMatchers.eq(json)
        )(ArgumentMatchers.any())).thenReturn(expectedOutcome))
    )
  }

  private[helpers] case class AuthConnectorPredicate(private val stubs: Seq[() => OngoingStubbing[_]]) extends MockPredicate(stubs) {
    def authenticatesWithResult[A](predicate: Predicate, retrieval: Retrieval[A], expectedOutcome: Future[A]): AuthConnectorPredicate =
      copy(
        stubs = stubs :+ (() => when(
          mockAuthConnector.authorise(
            ArgumentMatchers.eq(predicate),
            ArgumentMatchers.eq(retrieval)
          )(ArgumentMatchers.any(), ArgumentMatchers.any()))
          .thenReturn(expectedOutcome))
      )
  }

  abstract class MockPredicate(private val stubs: Seq[() => OngoingStubbing[_]] = Seq()) {
    def build(): Unit = stubs.foreach(_.apply())

    def and: MockPredicate = this
    def the: MockPredicate = this

    def resourceService: ResourceServicePredicate = ResourceServicePredicate(stubs)
    def validationService: ValidationServicePredicate = ValidationServicePredicate(stubs)
    def complianceCasesService: ComplianceCasesServicePredicate = ComplianceCasesServicePredicate(stubs)
    def complianceCasesConnector: ComplianceCasesConnectorPredicate = ComplianceCasesConnectorPredicate(stubs)
    def configuration: ConfigPredicate = ConfigPredicate(stubs)
    def authConnector: AuthConnectorPredicate = AuthConnectorPredicate(stubs)
  }
}