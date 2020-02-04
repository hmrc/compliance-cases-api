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

package models

import play.api.libs.json.Reads
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Address(
                         Line1: Option[String],
                         Line2: Option[String],
                         Line3: Option[String],
                         Town: Option[String],
                         Postcode: Option[String],
                         Phone: Option[String])

object Address {
  implicit def reads: Reads[Address] = (

    (__ \ "Line1").readNullable[String] and
      (__ \ "Line2").readNullable[String] and
      (__ \ "Line3").readNullable[String] and
      (__ \ "Town").readNullable[String] and
      (__ \ "Postcode").readNullable[String] and
      (__ \ "Phone").readNullable[String]

    ) (Address.apply _)
}
