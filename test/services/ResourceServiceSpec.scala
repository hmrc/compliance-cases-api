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

package services

import caseData.ComplianceCasesExamples._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import play.api.Environment

class ResourceServiceSpec extends AnyWordSpecLike with Matchers {

  private val env = Environment.simple()
  private val service = new ResourceService(env)

  "Service" should {

    "return exception" in {
      intercept[Exception](service.getFile("/schemas/1.json")).getLocalizedMessage shouldBe "resource not found: /schemas/1.json"
    }

    "return create case schema" in {
      service.getFile("/schemas/request.schema.json") shouldBe caseflowCreateCaseSchema
    }

    "return create repayment case schema" in {
      service.getFile("/schemas/repaymentCaseType.schema.json") shouldBe caseflowCreateRepaymentCaseSchema
    }

    "return create risk case schema" in {
      service.getFile("/schemas/riskCaseType.schema.json") shouldBe caseflowCreateRiskCaseSchema
    }
  }
}
