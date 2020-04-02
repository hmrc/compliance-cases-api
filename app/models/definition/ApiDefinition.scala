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

package models.definition

import play.api.libs.json.{JsArray, JsValue, Json, Writes}

case class ApiDefinition(whiteListedApplicationIds: Seq[String], endpointsEnabled: Boolean, status: String){
  val scopes: JsArray = Json.arr(
    Json.obj(
      "key" -> "write:compliance-cases-api",
      "name" -> "Create Cases",
      "description" -> "Scope to create cases in CaseFlow"
    )
  )

  val apiConfig: JsValue = Json.obj(
    "name" -> "Compliance Cases",
    "description" -> "Api to manage compliance cases in CaseFlow",
    "context" -> "misc/compliance-cases",
    "categories" -> Json.arr("OTHER"),
    "versions" -> Json.arr(
      Json.obj(
        "version" -> "1.0",
        "status" -> status,
        "endpointsEnabled" -> endpointsEnabled,
        "access" -> Json.obj(
          "type" -> "PRIVATE",
          "whitelistedApplicationIds" -> whiteListedApplicationIds
        )
      )
    )
  )
}


object ApiDefinition {
  implicit def definitionWrites: Writes[ApiDefinition] = (definition: ApiDefinition) => {
    Json.obj(
      "scopes" -> definition.scopes,
      "api" -> definition.apiConfig
    )
  }
}