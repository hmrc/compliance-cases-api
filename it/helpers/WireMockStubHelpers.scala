package helpers

import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.http.Fault
import com.github.tomakehurst.wiremock.stubbing.StubMapping

trait WireMockStubHelpers {

  def stubPostWithoutResponseBody(url: String, status: Int, correlationId: String): StubMapping =
    stubFor(post(urlMatching(url))
      .withHeader("CorrelationId", equalTo(correlationId))
      .withHeader("Authorization", equalTo("Bearer some-token"))
      .withHeader("Environment", equalTo("local"))
      .willReturn(
        aResponse()
          .withStatus(status)))

  def stubPostWithoutRequestAndResponseBody(url: String, status: Int): StubMapping =
    stubFor(post(urlMatching(url))
      .willReturn(
        aResponse()
          .withStatus(status)))

  def stubPostWithResponseBody(url: String, status: Int, correlationId: String, response: String): StubMapping =
    stubFor(post(urlMatching(url))
      .withHeader("CorrelationId", equalTo(correlationId))
      .withHeader("Authorization", equalTo("Bearer some-token"))
      .withHeader("Environment", equalTo("local"))
      .willReturn(
        aResponse()
          .withStatus(status)
          .withBody(response)
          .withHeader("Content-Type", "application/json; charset=utf-8")))

  def stubPostWithFault(url: String, correlationId: String): StubMapping =
    stubFor(post(urlMatching(url))
      .withHeader("CorrelationId", equalTo(correlationId))
      .withHeader("Authorization", equalTo("Bearer some-token"))
      .withHeader("Environment", equalTo("local"))
      .willReturn(
        aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)))
}
