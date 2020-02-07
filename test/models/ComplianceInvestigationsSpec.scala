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

import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json
import caseData.ComplianceCasesExamples._

class ComplianceInvestigationsSpec extends WordSpec with Matchers {

  val caseRef = "CSFC-1234567890"
  val name = "Mr Test Name"
  val age = 20
  val id = 150000

  "Compliance Investigations model" when {

    "binding json data to a model" should {
      "produce a full model" in {
        Json.fromJson[ComplianceInvestigations](Json.parse(minimumJson)).getOrElse(None) shouldBe filledMinimumModel
      }
      "return Errors if missing data" in {
        Json.fromJson[ComplianceInvestigations](Json.parse(incorrectJson)).isError shouldBe true
      }
    }
  }

}
