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

package models.responses

import play.api.libs.json.{Json, Writes}

case class BadRequestErrorResponse(code: String, message: String, caseType: Option[String], errors: Seq[FieldError])

object BadRequestErrorResponse {
  implicit def badRequestWrites: Writes[BadRequestErrorResponse] = Json.writes[BadRequestErrorResponse]

  def apply(errors: Seq[FieldError]): BadRequestErrorResponse = {
    new BadRequestErrorResponse("INVALID_PAYLOAD", "Submission has not passed validation. Invalid payload.", None, errors)
  }

  def apply(errors: Seq[FieldError], caseType: String): BadRequestErrorResponse = {
    new BadRequestErrorResponse("INVALID_PAYLOAD",
      s"Submission has not passed validation for the $caseType model. Invalid payload.",
      Some(caseType),
      errors
    )
  }
}
