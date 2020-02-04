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

case class CaseflowCaseModel(
                              SourceSysRef: String,
                              SourceSysID: String,
                              CaseFlowID: Int,
                              CampaignID: String,
                              ProjectID: String,
                              CaseType: String,
                              TargetRIS: Option[String],
                              ComplianceStream: Option[String],
                              EnguiryType: Option[String],
                              Segment: Option[String],
                              VATOfficeCode: String,
                              ProjectedYield: Option[String],
                              Filter1: Option[String],
                              Filter2: Option[String],
                              LastDateForEnquiry: Option[String],
                              ConfidenceScore: Option[Double],
                              ArchiveApproach: Option[String],
                              InterventionSubType: Option[String],
                              InteractionTitle: Option[String],
                              AuthorisationType: Option[String],
                              Risks: RisksModel,
                              Taxpayers: TaxpayersModel)

object CaseflowCaseModel {
  implicit def reads: Reads[CaseflowCaseModel] = (

    (__ \ "SourceSysRef").read[String] and
      (__ \ "SourceSysID").read[String] and
      (__ \ "CaseFlowID").read[Int] and
      (__ \ "CampaignID").read[String] and
      (__ \ "ProjectID").read[String] and
      (__ \ "CaseType").read[String] and
      (__ \ "TargetRIS").readNullable[String] and
      (__ \ "ComplianceStream").readNullable[String] and
      (__ \ "EnguiryType").readNullable[String] and
      (__ \ "Segment").readNullable[String] and
      (__ \ "VATOfficeCode").read[String] and
      (__ \ "ProjectedYield").readNullable[String] and
      (__ \ "Filter1").readNullable[String] and
      (__ \ "Filter2").readNullable[String] and
      (__ \ "LastDateForEnquiry").readNullable[String] and
      (__ \ "ConfidenceScore").readNullable[Double] and
      (__ \ "ArchiveApproach").readNullable[String] and
      (__ \ "InterventionSubType").readNullable[String] and
      (__ \ "InteractionTitle").readNullable[String] and
      (__ \ "AuthorisationType").readNullable[String] and
      (__ \ "Risks").read[RisksModel] and
      (__ \ "Taxpayers").read[TaxpayersModel]

    ) (CaseflowCaseModel.apply _)
}
