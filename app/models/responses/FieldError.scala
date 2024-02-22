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

package models.responses

import play.api.libs.json.{Json, Writes}

class FieldError(val code: String, val message: String, val path: String)

case class MissingField(override val path: String)
  extends FieldError(code = "MISSING_FIELD", message = "Expected field not present", path)

case class InvalidField(override val path: String)
  extends FieldError(code = "INVALID_FIELD", message = "Invalid value in field", path)

case class UnexpectedField(override val path: String)
  extends FieldError(code = "UNEXPECTED_FIELD", message = "Unexpected field found", path)

object InvalidJsonType
  extends FieldError(code = "INVALID_JSON_TYPE", message = "Invalid Json type as payload", path = "")

object FieldError{
  implicit def invalidFieldWrites: Writes[FieldError] = fieldError => {
    Json.obj(
      "code" -> fieldError.code,
      "message" -> fieldError.message,
      "path" -> fieldError.path
    )
  }
}
