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
import connectors.ComplianceCasesConnector
import javax.inject.{Inject, Singleton}
import play.api.mvc.{Action, AnyContent, ControllerComponents, Result}
import uk.gov.hmrc.play.bootstrap.controller.BackendController
import models.ComplianceInvestigationsModel
import play.api.http.ContentTypes
import play.api.libs.json.{JsError, JsNull, JsPath, JsSuccess, JsValue, Json, JsonValidationError}
import services.{ResourceService, ValidationService}
import uk.gov.hmrc.http.HttpResponse

import scala.concurrent.{ExecutionContext, Future}

@Singleton()
class ComplianceApiController @Inject()(
                                         validator: ValidationService,
                                         resources: ResourceService,
                                         complianceCasesConnector: ComplianceCasesConnector,
                                         appConfig: AppConfig,
                                         cc: ControllerComponents
                                       )(implicit ec: ExecutionContext) extends BackendController(cc) {

  private val schema = resources.getFile("/schemas/caseflowCreateCaseSchema.json")

  def risking(): Action[AnyContent] = Action.async {implicit request =>
    val input = request.body.asJson.getOrElse(JsNull)

    validator.validate(schema, input) match {
      case Right(_) => {
        Json.fromJson[ComplianceInvestigationsModel](input) match {
          case JsSuccess(value, path) => complianceCasesConnector.complianceInvestigations(Json.toJson(input)).map(mappingConnectorResponse)
          case JsError(errors) => Future.successful(BadRequest(mappingErrorResponse(errors)))
        }
      }
      case Left(errors) => Future.successful(BadRequest(errors))
    }
  }

  private def mappingErrorResponse(errors: Seq[(JsPath, Seq[JsonValidationError])]): JsValue = {
    val response = errors.map(x => Map(x._1.toString -> x._2.map(_.message)))
    Json.toJson(Map("MappingErrors" -> response))
  }

  private def mappingConnectorResponse(response: HttpResponse): Result = {
    val excludedHeaders = List(CONTENT_TYPE, CONTENT_LENGTH)

    val headers = for {
      (k, v) <- response.allHeaders
      if !excludedHeaders.contains(k)
    } yield k -> v.mkString(", ")

    Status(response.status)
      .apply(response.body)
      .withHeaders(headers.toList: _*)
      .as(ContentTypes.JSON)
  }

}
