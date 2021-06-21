/*
 * Copyright 2021 HM Revenue & Customs
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

import connectors.ComplianceCasesConnector
import models.LogMessageHelper
import play.api.Logger

import javax.inject.Inject
import play.api.libs.json.JsValue
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}

import scala.concurrent.{ExecutionContext, Future}

class ComplianceCasesService @Inject()(connector: ComplianceCasesConnector) {

  val className: String = this.getClass.getSimpleName
  val logger: Logger = Logger(className)


  def createCase(request: JsValue, correlationId: String)
                (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Option[HttpResponse]] = {
  def logMessage(message: String): String = LogMessageHelper(className, "createCase", message, correlationId).toString
    logger.warn(logMessage(s"request being sent from ComplianceCaseService with Request: $request"))
    connector.createCase(request, correlationId)
  }
}
