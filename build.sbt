import play.core.PlayVersion
import play.sbt.PlayScala
import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings.integrationTestSettings
import uk.gov.hmrc.SbtArtifactory
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings

val appName = "compliance-cases-api"

scalaVersion := "2.12.11"
majorVersion := 0
PlayKeys.playDefaultPort := 7052

lazy val microservice = Project(appName, file("."))
  .configs(IntegrationTest)

libraryDependencies  ++= Seq(
  "uk.gov.hmrc"                 %% "bootstrap-play-26"        % "1.7.0",
  "com.github.java-json-tools"  % "json-schema-validator"     % "2.2.13",
  "uk.gov.hmrc"                 %% "play-hmrc-api"            % "4.1.0-play-26",
  "org.scalatest"               %% "scalatest"                % "3.0.8"                 % "test",
  "com.typesafe.play"           %% "play-test"                % PlayVersion.current     % "test",
  "org.pegdown"                 %  "pegdown"                  % "1.6.0"                 % "test, it",
  "org.scalatestplus.play"      %% "scalatestplus-play"       % "3.1.3"                 % "test, it",
  "org.mockito"                 % "mockito-all"               % "1.10.19"               % "test",
  "com.github.tomakehurst"      % "wiremock-standalone"       % "2.26.3"                % "test"
)

ScoverageKeys.coverageExcludedFiles := "<empty>;Reverse.*;.*Routes.*;.*GuiceInjector;"
ScoverageKeys.coverageMinimum := 70
ScoverageKeys.coverageFailOnMinimum := true
ScoverageKeys.coverageHighlighting := true

publishingSettings
integrationTestSettings
resolvers += Resolver.jcenterRepo

enablePlugins(PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin, SbtArtifactory)
