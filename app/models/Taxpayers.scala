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

case class TaxpayersModel(Taxpayer: TaxpayerModel)

object TaxpayersModel {
  implicit def reads: Reads[TaxpayersModel] = (__ \ "Taxpayer").read[TaxpayerModel].map(TaxpayersModel.apply)
}

case class TaxpayerModel(
                          Type: String,
                          OUID: Option[String],
                          Title: Option[String],
                          FirstName: Option[String],
                          Surname: Option[String],
                          NINO: Option[String],
                          UTR: Option[String],
                          EmpPAYERef: Option[String],
                          VATNumber: Option[String],
                          Reference: Option[String],
                          TypeOfReference: Option[String],
                          ValueOfReference: Option[String],
                          HomeAddress: Option[AddressModel],
                          BusinessName: Option[String],
                          BusinessAddress: Option[AddressModel],
                          email: Option[String])

object TaxpayerModel {
  implicit def reads: Reads[TaxpayerModel] = (

    (__ \ "Type").read[String] and
      (__ \ "OUID").readNullable[String] and
      (__ \ "Title").readNullable[String] and
      (__ \ "FirstName").readNullable[String] and
      (__ \ "Surname").readNullable[String] and
      (__ \ "NINO").readNullable[String] and
      (__ \ "UTR").readNullable[String] and
      (__ \ "EmpPAYERef").readNullable[String] and
      (__ \ "VATNumber").readNullable[String] and
      (__ \ "Reference").readNullable[String] and
      (__ \ "TypeOfReference").readNullable[String] and
      (__ \ "ValueOfReference").readNullable[String] and
      (__ \ "HomeAddress").readNullable[AddressModel] and
      (__ \ "BusinessName").readNullable[String] and
      (__ \ "BusinessAddress").readNullable[AddressModel] and
      (__ \ "email").readNullable[String]

    ) (TaxpayerModel.apply _)
}

