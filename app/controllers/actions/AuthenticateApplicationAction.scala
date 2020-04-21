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

package controllers.actions

import javax.inject.Inject
import models.LogMessageHelper
import play.api.libs.json.Json
import play.api.mvc.Results.{InternalServerError, Unauthorized}
import play.api.mvc._
import play.api.{Configuration, Logger}
import uk.gov.hmrc.api.controllers.{ErrorInternalServerError, ErrorUnauthorized}
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.auth.core.{AuthProvider, AuthProviders, AuthorisationException, AuthorisedFunctions}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.HeaderCarrierConverter
import uk.gov.hmrc.play.bootstrap.auth.DefaultAuthConnector

import scala.concurrent.{ExecutionContext, Future}


class AuthenticateApplicationAction @Inject()(
                                               val authConnector: DefaultAuthConnector,
                                               config: Configuration,
                                               val parser: BodyParsers.Default
                                             )(implicit val executionContext: ExecutionContext) extends
  AuthorisedFunctions with ActionBuilder[Request, AnyContent] {

  lazy val applicationIdIsAllowed: Set[String] = config.get[Option[Seq[String]]]("apiDefinition.whitelistedApplicationIds")
    .getOrElse(Seq.empty[String])
    .toSet

  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
    implicit val hc: HeaderCarrier =
      HeaderCarrierConverter.fromHeadersAndSessionAndRequest(request.headers, request = Some(request))

    authorised(AuthProviders(AuthProvider.StandardApplication)).retrieve(Retrievals.applicationId) {
      case Some(applicationId) if applicationIdIsAllowed(applicationId) =>
        block(request)
      case _ =>
        Logger.warn(
          LogMessageHelper("AuthenticateApplicationAction", "invokeBlock", "no application id or application id not in request").toString
        )
        Future.successful(Unauthorized(Json.toJson(ErrorUnauthorized)))
    } recover {
      case _: AuthorisationException =>
        Logger.warn(
          LogMessageHelper("AuthenticateApplicationAction", "invokeBlock", "no application id or application id not in request").toString
        )
        Unauthorized(Json.toJson(ErrorUnauthorized))
      case e: Throwable =>
        Logger.warn(
          LogMessageHelper("AuthenticateApplicationAction", "invokeBlock", "an unexpected exception occurred").toString, e
        )
        InternalServerError(Json.toJson(ErrorInternalServerError))
    }
  }
}
