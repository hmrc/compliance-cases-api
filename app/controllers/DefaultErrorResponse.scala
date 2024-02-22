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

package controllers

import play.api.http.Status._
import play.api.libs.json._

sealed trait DefaultErrorResponse extends Product with Serializable {
  val httpStatusCode: Int
  val errorCode:      String
  val message:        String
}

case object ErrorUnauthorized extends DefaultErrorResponse {
  override val httpStatusCode: Int = UNAUTHORIZED
  override val errorCode: String = "UNAUTHORIZED"
  override val message: String = "Bearer token is missing or not authorized"
}

case class ErrorGenericBadRequest(msg: String = "Bad Request") extends DefaultErrorResponse {
  override val httpStatusCode: Int = BAD_REQUEST
  override val errorCode: String = "BAD_REQUEST"
  override val message: String = msg
}

case object ErrorInternalServerError extends DefaultErrorResponse {
  override val httpStatusCode: Int = INTERNAL_SERVER_ERROR
  override val errorCode: String = "INTERNAL_SERVER_ERROR"
  override val message: String = "Internal server error"
}

object DefaultErrorResponse {
    implicit val writes = new Writes[DefaultErrorResponse] {
      def writes(e: DefaultErrorResponse): JsValue ={
        e match {
          case ErrorUnauthorized => Json.obj("code" -> e.errorCode, "message" -> e.message)
          case ErrorGenericBadRequest(msg) => Json.obj("code" -> e.errorCode, "message" -> msg)
          case ErrorInternalServerError => Json.obj("code" -> e.errorCode, "message" -> e.message)
        }

      }
    }
  }



