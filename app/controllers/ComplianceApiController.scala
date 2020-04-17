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

package controllers

import config.AppConfig
import controllers.actions.{AuthenticateApplicationAction, ValidateCorrelationIdHeaderAction}
import javax.inject.{Inject, Singleton}
import models.{ComplianceInvestigations, LogMessageHelper}
import play.api.Logger
import play.api.http.ContentTypes
import play.api.libs.json.{JsNull, Json}
import play.api.mvc.{Action, AnyContent, ControllerComponents, Result}
import services.{ComplianceCasesService, ResourceService, ValidationService}
import uk.gov.hmrc.api.controllers.ErrorInternalServerError
import uk.gov.hmrc.http.HttpResponse
import uk.gov.hmrc.play.bootstrap.controller.BackendController

import scala.concurrent.{ExecutionContext, Future}

@Singleton()
class ComplianceApiController @Inject()(
                                         validator: ValidationService,
                                         resources: ResourceService,
                                         complianceCasesService: ComplianceCasesService,
                                         appConfig: AppConfig,
                                         getCorrelationId: ValidateCorrelationIdHeaderAction,
                                         authenticateApplication: AuthenticateApplicationAction,
                                         cc: ControllerComponents
                                       )(implicit ec: ExecutionContext) extends BackendController(cc) {

  private val schema = resources.getFile("/schemas/caseflowCreateCaseSchema.json")

  def createCase(): Action[AnyContent] = (authenticateApplication andThen getCorrelationId).async { implicit request =>
    val input = request.body.asJson.getOrElse(JsNull)

    def logMessage(message: String): String = LogMessageHelper("ComplianceApiController", "createCase", message, request.correlationId).toString

    validator.validate[ComplianceInvestigations](schema, input) match {
      case Right(_) =>
        Logger.info(logMessage("request received passing on to integration framework"))
        complianceCasesService.createCase(Json.toJson(input), request.correlationId).map(
          response => response.fold[Result](_ => InternalServerError(Json.toJson(ErrorInternalServerError)), mappingConnectorResponse)
        )
      case Left(errors) =>
        Logger.warn(logMessage(s"request body didn't match json with errors: ${Json.prettyPrint(errors)}"))
        Future.successful(BadRequest(errors))
    }
  }

  private def mappingConnectorResponse(response: HttpResponse): Result = {
    val excludedHeaders = List(CONTENT_TYPE, CONTENT_LENGTH)

    val headers = for {
      (key, values) <- response.allHeaders
      if !excludedHeaders.contains(key)
    } yield key -> values.mkString(", ")

    Status(response.status)
      .apply(response.body)
      .withHeaders(headers.toList: _*)
      .as(ContentTypes.JSON)
  }

}
