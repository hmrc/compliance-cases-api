package api

import helpers.{Fixtures, WireMockSpec}
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import play.api.test.Helpers._

class CreateCaseISpec extends PlaySpec with WireMockSpec with Fixtures {

  "POST /case" should {
    s"return an $ACCEPTED if $ACCEPTED received from IF for repayments" in {
      stubPostWithoutResponseBody("/organisations/case", ACCEPTED, correlationId)
      stubPostWithResponseBody("/auth/authorise", ACCEPTED, Json.obj(
        "applicationId" -> "ID-1"
      ).toString)
      stubPostWithoutRequestAndResponseBody("/write/audit", NO_CONTENT)
      stubPostWithoutRequestAndResponseBody("/write/audit/merged", NO_CONTENT)

      val response = await(buildClient("/case")
        .withHttpHeaders("CorrelationId" -> correlationId, "X-Request-Id" -> "one-two-three")
        .post(createRepaymentCaseJson))

      response.status mustBe ACCEPTED
      response.body mustBe ""
    }

    s"return an $ACCEPTED if $ACCEPTED received from IF for risk" in {
      stubPostWithoutResponseBody("/organisations/case", ACCEPTED, correlationId)
      stubPostWithResponseBody("/auth/authorise", ACCEPTED, Json.obj(
        "applicationId" -> "ID-1"
      ).toString)
      stubPostWithoutRequestAndResponseBody("/write/audit", NO_CONTENT)
      stubPostWithoutRequestAndResponseBody("/write/audit/merged", NO_CONTENT)

      val response = await(buildClient("/case")
        .withHttpHeaders("CorrelationId" -> correlationId, "X-Request-Id" -> "one-two-three")
        .post(createCaseRiskJson))

      response.status mustBe ACCEPTED
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
        .withHttpHeaders("CorrelationId" -> correlationId, "X-Request-Id" -> "one-two-three")
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
    s"return a $UNAUTHORIZED if application is allowListed" in {
      stubPostWithResponseBody("/auth/authorise", ACCEPTED, Json.obj(
        "applicationId" -> "ID-7"
      ).toString)
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
        .withHttpHeaders("CorrelationId" -> correlationId, "X-Request-Id" -> "one-two-three")
        .post(createRepaymentCaseJson))

      response.status mustBe INTERNAL_SERVER_ERROR
      response.body mustBe """{"code":"INTERNAL_SERVER_ERROR","message":"Internal server error"}"""
    }
  }
}
