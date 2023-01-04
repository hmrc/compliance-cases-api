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

package helpers

import connectors.ComplianceCasesConnector
import controllers.actions.{AuthenticateApplicationAction, RequestWithCorrelationId}
import org.scalamock.handlers.CallHandler
import org.scalamock.scalatest.MockFactory
import play.api.{ConfigLoader, Configuration}
import play.api.libs.json.JsValue
import services.{ComplianceCasesService, ResourceService, ValidationService}
import uk.gov.hmrc.auth.core.AuthConnector
import uk.gov.hmrc.auth.core.authorise.Predicate
import uk.gov.hmrc.auth.core.retrieve.Retrieval
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}

import scala.concurrent.{ExecutionContext, Future}

trait MockHelpers extends MockFactory {

  lazy val mockResourceService: ResourceService = mock[ResourceService]
  lazy val mockValidationService: ValidationService = mock[ValidationService]
  lazy val mockComplianceCasesService: ComplianceCasesService = mock[ComplianceCasesService]
  lazy val mockComplianceCasesConnector: ComplianceCasesConnector = mock[ComplianceCasesConnector]
  lazy val mockAuthConnector: AuthConnector = mock[AuthConnector]
  lazy val mockConfig: Configuration = mock[Configuration]
  lazy val mockAuthApplicationAction: AuthenticateApplicationAction = mock[AuthenticateApplicationAction]

  object Given extends MockPredicate()

  private[helpers] case class ConfigPredicate(private val stubs: Seq[() => CallHandler[_]]) extends MockPredicate(stubs){
    def getsConfigAt[A](address: String, configValue: A): ConfigPredicate = copy(
      stubs = stubs :+ (() => (mockConfig.get[A](_: String)(_: ConfigLoader[A]))
        .expects(address, *)
        .once()
        .returns(configValue))
    )
  }

  private[helpers] case class ResourceServicePredicate(private val stubs: Seq[() => CallHandler[_]]) extends MockPredicate(stubs){
    def returnsResourceAt(address: String, resourceAsString: String): ResourceServicePredicate = copy(
      stubs = stubs :+ (() => (mockResourceService.getFile(_: String))
        .expects(address)
        .once()
        .returns(resourceAsString))
    )
  }

  private[helpers] case class ComplianceCasesServicePredicate(private val stubs: Seq[() => CallHandler[_]]) extends MockPredicate(stubs){
    def createsCase(input: JsValue, correlationId: String, httpResponse: Option[HttpResponse]): ComplianceCasesServicePredicate = copy(
      stubs = stubs :+ (() => (mockComplianceCasesService.createCase(_: JsValue, _: String)(_: HeaderCarrier, _: ExecutionContext))
        .expects(input, correlationId, *, *)
        .once()
        .returns(Future.successful(httpResponse)))
    )
  }

  private[helpers] case class ComplianceCasesConnectorPredicate(private val stubs: Seq[() => CallHandler[_]]) extends MockPredicate(stubs){
    def createsCase(input: JsValue, correlationId: String, httpResponse: Option[HttpResponse]): ComplianceCasesConnectorPredicate = copy(
      stubs = stubs :+ (() => (mockComplianceCasesConnector.createCase(_: JsValue, _: String)(_: HeaderCarrier, _: ExecutionContext))
        .expects(input, correlationId, *, *)
        .once()
        .returns(Future.successful(httpResponse)))
    )
  }

  private[helpers] case class ValidationServicePredicate(private val stubs: Seq[() => CallHandler[_]]) extends MockPredicate(stubs){
    def validate(schema: String, json: JsValue, expectedOutcome: Option[JsValue]): ValidationServicePredicate = copy(
      stubs = stubs :+ (() => (mockValidationService.validateAndRetrieveErrors(_: String, _: JsValue)(_: RequestWithCorrelationId[_]))
        .expects(schema, json, *)
        .once()
        .returns(expectedOutcome))
    )
  }

  private[helpers] case class AuthConnectorPredicate(private val stubs: Seq[() => CallHandler[_]]) extends MockPredicate(stubs){
    def authenticatesWithResult[A](predicate: Predicate, retrieval: Retrieval[A], expectedOutcome: Future[A]): AuthConnectorPredicate =
      copy(
        stubs = stubs :+ (() => (mockAuthConnector.authorise(_: Predicate, _: Retrieval[A])(_: HeaderCarrier, _: ExecutionContext))
          .expects(predicate, retrieval, *, *)
          .once()
          .returns(expectedOutcome))
      )
  }

  abstract class MockPredicate(private val stubs: Seq[() => CallHandler[_]]= Seq()){
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
