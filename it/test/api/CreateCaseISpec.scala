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

package api

import helpers.{Fixtures, WireMockSpec}
import org.apache.pekko.util.Timeout
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import play.api.libs.ws.DefaultBodyReadables.readableAsString
import play.api.libs.ws.writeableOf_JsValue
import play.api.test.Helpers.*

import scala.concurrent.duration.Duration

class CreateCaseISpec extends PlaySpec with WireMockSpec with Fixtures {
  implicit val timeout:Timeout = Timeout.durationToTimeout(Duration.create(30,"s"))
  "POST /case" should {
    s"return an $OK if $OK received from IF for repayments" in {
      stubPostWithoutResponseBody("/organisations/case", OK, correlationId)
      stubPostWithResponseBody("/auth/authorise", OK, Json.obj(
        "applicationId" -> "ID-1"
      ).toString)
      stubPostWithoutRequestAndResponseBody("/write/audit", NO_CONTENT)
      stubPostWithoutRequestAndResponseBody("/write/audit/merged", NO_CONTENT)

      val response = await(buildClient("/case")
        .withHttpHeaders("CorrelationId" -> correlationId, "X-Request-Id" -> "one-two-three",
          AUTHORIZATION -> "Bearer some-token")
        .post(createRepaymentCaseJson))

      response.status mustBe OK
      response.body mustBe ""
    }

    s"return an $OK if $OK received from IF for risk" in {
      stubPostWithoutResponseBody("/organisations/case", OK, correlationId)
      stubPostWithResponseBody("/auth/authorise", OK, Json.obj(
        "applicationId" -> "ID-1"
      ).toString)
      stubPostWithoutRequestAndResponseBody("/write/audit", NO_CONTENT)
      stubPostWithoutRequestAndResponseBody("/write/audit/merged", NO_CONTENT)

      val response = await(buildClient("/case")
        .withHttpHeaders("CorrelationId" -> correlationId, "X-Request-Id" -> "one-two-three", AUTHORIZATION -> "Bearer some-token")
        .post(createCaseRiskJson))

      response.status mustBe OK
      response.body mustBe ""
    }

    s"return a $INTERNAL_SERVER_ERROR if $BAD_REQUEST received from IF" in {
      stubPostWithResponseBodyAndHeaders("/organisations/case", BAD_REQUEST, correlationId, Json.obj(
        "code" -> "BAD_REQUEST", "message" -> "oops something in there is bad"
      ).toString)
      stubPostWithResponseBody("/auth/authorise", ACCEPTED, Json.obj(
        "applicationId" -> "ID-1"
      ).toString)
      stubPostWithoutRequestAndResponseBody("/write/audit", NO_CONTENT)
      stubPostWithoutRequestAndResponseBody("/write/audit/merged", NO_CONTENT)

      val response = await(buildClient("/case")
        .withHttpHeaders("CorrelationId" -> correlationId, "X-Request-Id" -> "one-two-three", AUTHORIZATION -> "Bearer some-token")
        .post(createRepaymentCaseJson))

      response.status mustBe INTERNAL_SERVER_ERROR
      response.body mustBe """{"code":"INTERNAL_SERVER_ERROR","message":"Internal server error"}"""
    }

    s"return a $UNAUTHORIZED if application is not authorised" in {
      stubPostWithResponseBody("/auth/authorise", UNAUTHORIZED, "{}")
      stubPostWithoutRequestAndResponseBody("/write/audit", NO_CONTENT)
      stubPostWithoutRequestAndResponseBody("/write/audit/merged", NO_CONTENT)

      val response = await(buildClient("/case")
        .withHttpHeaders("CorrelationId" -> correlationId, "X-Request-Id" -> "one-two-three")
        .post(createRepaymentCaseJson))

      response.status mustBe UNAUTHORIZED
      response.body mustBe """{"code":"UNAUTHORIZED","message":"Bearer token is missing or not authorized"}"""
    }

    s"return a $INTERNAL_SERVER_ERROR if an exception occurs received from IF" in {
      stubPostWithFault("/organisations/case", correlationId)
      stubPostWithoutRequestAndResponseBody("/write/audit", NO_CONTENT)
      stubPostWithoutRequestAndResponseBody("/write/audit/merged", NO_CONTENT)
      stubPostWithResponseBody("/auth/authorise", ACCEPTED, Json.obj(
        "applicationId" -> "ID-1"
      ).toString)

      val response = await(buildClient("/case")
        .withHttpHeaders("CorrelationId" -> correlationId, "X-Request-Id" -> "one-two-three", AUTHORIZATION -> "Bearer some-token")
        .post(createRepaymentCaseJson))

      response.status mustBe INTERNAL_SERVER_ERROR
      response.body mustBe """{"code":"INTERNAL_SERVER_ERROR","message":"Internal server error"}"""
    }
  }
}
