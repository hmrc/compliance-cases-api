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

package controllers.definition

import controllers.Assets
import javax.inject.Inject
import models.definition.ApiDefinition
import play.api.Configuration
import play.api.http.HttpErrorHandler
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.api.controllers.DocumentationController

class ApiDocumentationController @Inject()(
                                            cc: ControllerComponents,
                                            assets: Assets,
                                            errorHandler: HttpErrorHandler,
                                            config: Configuration) extends DocumentationController(cc, assets, errorHandler){

  lazy val whitelistedApplicationIds: Seq[String] = config.getOptional[Seq[String]]("apiDefinition.whitelistedApplicationIds").getOrElse(Seq.empty)

  lazy val status: String = config.get[String]("apiDefinition.status")

  lazy val endpointsEnabled: Boolean = config.get[Boolean]("apiDefinition.endpointsEnabled")

  override def definition(): Action[AnyContent] = cc.actionBuilder {
    Ok(Json.toJson(ApiDefinition(whitelistedApplicationIds, endpointsEnabled, status)))
  }
}
