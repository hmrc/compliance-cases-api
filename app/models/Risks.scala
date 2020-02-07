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

case class Risks(Risk: Risk)

object Risks {
  implicit def reads: Reads[Risks] = (__ \ "Risk").read[Risk].map(Risks.apply)
}

case class Risk(
                      TaxRegime: String,
                      Description: String,
                      Score: Double,
                      TaxPeriodFrom: String,
                      TaxPeriodTo: String,
                      SubRegime: Option[String],
                      ComplianceCheck1: Option[String],
                      ComplianceCheck2: Option[String],
                      InaccuracyCategory: Option[String],
                      InaccuracyDescription: Option[String],
                      PotentialAmount: Option[Double],
                      ExpectedAmount: Option[Double],
                      PotentialBehaviour: Option[String],
                      EmergingBehaviour: Option[String])

object Risk {
  implicit def reads: Reads[Risk] = (

    (__ \ "TaxRegime").read[String] and
      (__ \ "Description").read[String] and
      (__ \ "Score").read[Double] and
      (__ \ "TaxPeriodFrom").read[String] and
      (__ \ "TaxPeriodTo").read[String] and
      (__ \ "SubRegime").readNullable[String] and
      (__ \ "ComplianceCheck1").readNullable[String] and
      (__ \ "ComplianceCheck2").readNullable[String] and
      (__ \ "InaccuracyCategory").readNullable[String] and
      (__ \ "InaccuracyDescription").readNullable[String] and
      (__ \ "PotentialAmount").readNullable[Double] and
      (__ \ "ExpectedAmount").readNullable[Double] and
      (__ \ "PotentialBehaviour").readNullable[String] and
      (__ \ "EmergingBehaviour").readNullable[String]

    ) (Risk.apply _)
}

