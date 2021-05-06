/*
 * Copyright 2021 HM Revenue & Customs
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

package controllers

import controllers.actions.{AuthenticateApplicationAction, ValidateCorrelationIdHeaderAction}
import javax.inject.Inject
import models.LogMessageHelper
import play.api.Logger
import play.api.libs.json.{JsNull, Json}
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import services.{ComplianceCasesService, ResourceService, ValidationService}
import uk.gov.hmrc.api.controllers.ErrorInternalServerError
import uk.gov.hmrc.play.bootstrap.controller.BackendController

import scala.concurrent.{ExecutionContext, Future}

class ComplianceApiController @Inject()(
  validator: ValidationService,
  resources: ResourceService,
  complianceCasesService: ComplianceCasesService,
  getCorrelationId: ValidateCorrelationIdHeaderAction,
  authenticateApplication: AuthenticateApplicationAction,
  cc: ControllerComponents
)(
  implicit ec: ExecutionContext
) extends BackendController(cc) {

  private lazy val schema = resources.getFile("/schemas/request.schema.json")

  private val logger = Logger(this.getClass.getSimpleName)

  def createCase(): Action[AnyContent] = (authenticateApplication andThen getCorrelationId).async { implicit request =>
    val input = request.body.asJson.getOrElse(JsNull)

    def logMessage(message: String): String =
      LogMessageHelper("ComplianceApiController", "createCase", message, request.correlationId).toString

    validator.validateAndRetrieveErrors(schema, input) match {
      case None =>
        logger.info(logMessage("request received passing on to integration framework"))
        complianceCasesService.createCase(Json.toJson(input), request.correlationId).map(
          maybeResponse => maybeResponse.fold(
            ifEmpty = InternalServerError(Json.toJson(ErrorInternalServerError))
          )(
            response => Status(response.status)(response.body)
          )
        )
      case Some(errors) =>
        logger.warn(logMessage(s"request body didn't match json with errors: ${Json.prettyPrint(errors)}"))
        Future.successful(BadRequest(errors))
    }
  }

}
