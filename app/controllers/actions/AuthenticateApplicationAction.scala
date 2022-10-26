/*
 * Copyright 2022 HM Revenue & Customs
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

import controllers.{DefaultErrorResponse, ErrorInternalServerError, ErrorUnauthorized}

import javax.inject.Inject
import models.LogMessageHelper
import org.slf4j.MDC
import play.api.libs.json.Json
import play.api.mvc.Results.{InternalServerError, Unauthorized}
import play.api.mvc._
import play.api.Logger
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.http.{HeaderCarrier, HeaderNames}
import uk.gov.hmrc.play.http.HeaderCarrierConverter

import scala.concurrent.{ExecutionContext, Future}


class AuthenticateApplicationAction @Inject()(
  val authConnector: AuthConnector,
  val parser: BodyParsers.Default
)(implicit val executionContext: ExecutionContext) extends
  AuthorisedFunctions with ActionBuilder[Request, AnyContent] {


  private val logger = Logger(this.getClass.getSimpleName)

  private[actions] def updateContextWithRequestId(implicit hc: HeaderCarrier): Unit = {
    if(Option(MDC.getCopyOfContextMap).isEmpty || MDC.getCopyOfContextMap.isEmpty) {
      hc.requestId.foreach(id => MDC.put(HeaderNames.xRequestId, id.value))
      hc.sessionId.foreach(id => MDC.put(HeaderNames.xSessionId, id.value))
    }
  }

  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromRequest(request = request)

    updateContextWithRequestId

    authorised(AuthProviders(AuthProvider.StandardApplication)).retrieve(Retrievals.applicationId) {
      case Some(_) =>
        block(request)
      case None =>
        logger.warn(
          LogMessageHelper("AuthenticateApplicationAction", "invokeBlock", "no application id or application id not in request").toString
        )
        Future.successful(Unauthorized(Json.toJson(ErrorUnauthorized)))
    } recover {
      case _: AuthorisationException =>
        logger.warn(
          LogMessageHelper("AuthenticateApplicationAction", "invokeBlock", "no application id or application id not in request").toString
        )
        Unauthorized(Json.toJson(ErrorUnauthorized))
      case e: Throwable =>
        logger.warn(
          LogMessageHelper("AuthenticateApplicationAction", "invokeBlock", "an unexpected exception occurred").toString, e
        )
        InternalServerError(Json.toJson(ErrorInternalServerError))
    }
  }
}
