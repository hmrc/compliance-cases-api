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

package controllers

import play.api.http.Status._
import play.api.libs.json._

sealed trait DefaultErrorResponse extends Product with Serializable {
  val httpStatusCode: Int
  val errorCode:      String
  val message:        String
  }


case class ErrorUnauthorized(httpStatusCode: Int = UNAUTHORIZED,
                             errorCode: String = "UNAUTHORIZED",
                             message: String = "Bearer token is missing or not authorized")
  extends DefaultErrorResponse
object ErrorUnauthorized {
  implicit val writes = new Writes[ErrorUnauthorized] {
    override def writes(o: ErrorUnauthorized): JsValue = Json.obj("code" -> o.errorCode, "message" -> o.message)
  }

}

case class ErrorGenericBadRequest(httpStatusCode: Int = BAD_REQUEST,
                                  errorCode: String = "BAD_REQUEST",
                                  message: String = "Bad Request")
  extends DefaultErrorResponse

object ErrorGenericBadRequest {
  implicit val writes = new Writes[ErrorGenericBadRequest] {
    override def writes(o: ErrorGenericBadRequest): JsValue = Json.obj("code" -> o.errorCode, "message" -> o.message)
  }
}

case class ErrorInternalServerError(httpStatusCode: Int = INTERNAL_SERVER_ERROR,
                                    errorCode: String = "INTERNAL_SERVER_ERROR",
                                    message: String = "Internal server error")
  extends DefaultErrorResponse

object ErrorInternalServerError {
  implicit val writes = new Writes[ErrorInternalServerError] {
    override def writes(o: ErrorInternalServerError): JsValue = Json.obj("code" -> o.errorCode, "message" -> o.message)
  }
}


object DefaultErrorResponse {


  implicit val writes = new Writes[DefaultErrorResponse] {
    def writes(e: DefaultErrorResponse): JsValue = {
      e match {
        case un: ErrorUnauthorized => Json.writes[ErrorUnauthorized].writes(un)
        case br: ErrorGenericBadRequest => Json.writes[ErrorGenericBadRequest].writes(br)
        case is: ErrorInternalServerError => Json.writes[ErrorInternalServerError].writes(is)
      }

    }
  }

}

