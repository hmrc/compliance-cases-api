
package helpers

import com.codahale.metrics.SharedMetricRegistries
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import com.github.tomakehurst.wiremock.client.WireMock.reset
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.ws.{WSClient, WSRequest}
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}

import scala.concurrent.ExecutionContext

trait WireMockSpec extends BeforeAndAfterEach with BeforeAndAfterAll with GuiceOneServerPerSuite
  with FutureAwaits with DefaultAwaitTimeout with WireMockStubHelpers {
  self: PlaySpec =>

  val wireMockPort = 11111

  lazy val ws: WSClient = app.injector.instanceOf(classOf[WSClient])
  implicit val ec: ExecutionContext = ExecutionContext.global

  val wireMockServer: WireMockServer = new WireMockServer(wireMockConfig().port(wireMockPort))

  override implicit lazy val app = GuiceApplicationBuilder()
    .configure(
      "auditing.consumer.baseUri.port" -> wireMockPort,
      "integration-framework.base-url" -> s"http://localhost:$wireMockPort",
      "microservice.services.auth.port" -> s"$wireMockPort"
    )
    .build()

  override def beforeAll(): Unit = {
    super.beforeAll()
    wireMockServer.start()
    SharedMetricRegistries.clear()
    WireMock.configureFor("localhost", wireMockPort)
  }

  override def afterAll(): Unit = {
    super.afterAll()
    wireMockServer.stop()
  }

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset()
  }

  def buildClient(uri: String, port: Int = port): WSRequest = ws
    .url(s"http://localhost:$port$uri")
    .withFollowRedirects(false)

}
