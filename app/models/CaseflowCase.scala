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

trait CaseflowCase

case class RepaymentCase(
                         caseType: String,
                         sourceSystemRef: String,
                         repaymentAmount: Double,
                         taxRegime: String,
                         taxPeriodStart: String,
                         taxPeriodEnd: String,
                         caseOwnerId: Option[Long],
                         triggeredRiskRuleRef: Option[String],
                         taxPayer: TaxPayer
                       ) extends CaseflowCase

case class RiskCase(
                   caseType: String,
                   sourceSystemRef: String
                   ) extends CaseflowCase

object CaseflowCase {
  implicit def caseFlowCaseReads: Reads[RepaymentCase] = Json.reads[RepaymentCase]

  implicit def riskCaseReads: Reads[RiskCase] = Json.reads[RiskCase]

  implicit def casesReads: Reads[CaseflowCase] = (json: JsValue) => {
    json.validate[RepaymentCase] orElse json.validate[RiskCase]
  }
}
