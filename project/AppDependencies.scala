import play.core.PlayVersion.current
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc"                 %% "bootstrap-play-26"    % "1.6.0",
    "com.github.java-json-tools"  % "json-schema-validator" % "2.2.13",
    "uk.gov.hmrc"                 %% "play-hmrc-api"        % "4.1.0-play-26"
  )

  val test = Seq(
    "org.scalatest"           %% "scalatest"                % "3.0.8"                 % "test",
    "com.typesafe.play"       %% "play-test"                % current                 % "test",
    "org.pegdown"             %  "pegdown"                  % "1.6.0"                 % "test, it",
    "org.scalatestplus.play"  %% "scalatestplus-play"       % "3.1.3"                 % "test, it",
    "org.mockito"             % "mockito-all"               % "1.10.19"               % "test",
    "com.github.tomakehurst"  % "wiremock-standalone"       % "2.26.3"                % "test"
  )

}
