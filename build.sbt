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
  .disablePlugins(JUnitXmlReportPlugin)
  .configs(IntegrationTest)

// ***************
// Use the silencer plugin to suppress warnings from unused imports in compiled twirl templates
val silencerVersion = "1.7.0"
libraryDependencies ++= Seq(
  compilerPlugin("com.github.ghik" % "silencer-plugin" % silencerVersion cross CrossVersion.full),
  "com.github.ghik" % "silencer-lib" % silencerVersion % Provided cross CrossVersion.full
)
// ***************
scalacOptions ++= Seq(
  "-P:silencer:pathFilters=views;routes"
)

libraryDependencies  ++= Seq(
  "uk.gov.hmrc"                 %% "bootstrap-play-26"        % "3.2.0",
  "com.github.java-json-tools"  % "json-schema-validator"     % "2.2.14",
  "uk.gov.hmrc"                 %% "play-hmrc-api"            % "4.1.0-play-26",
  "uk.gov.hmrc"                 %% "auth-client"              % "3.0.0-play-26",
  "org.scalatest"               %% "scalatest"                % "3.0.8"                 % "test",
  "com.typesafe.play"           %% "play-test"                % PlayVersion.current     % "test",
  "org.pegdown"                 %  "pegdown"                  % "1.6.0"                 % "test, it",
  "org.scalatestplus.play"      %% "scalatestplus-play"       % "3.1.3"                 % "test, it",
  "org.scalamock"               %% "scalamock"                % "4.4.0"                 % "test",
  "com.github.tomakehurst"      % "wiremock-standalone"       % "2.26.3"                % "test, it"
)

ScoverageKeys.coverageExcludedFiles := "<empty>;Reverse.*;.*Routes.*;.*GuiceInjector;"
ScoverageKeys.coverageMinimum := 100
ScoverageKeys.coverageFailOnMinimum := true
ScoverageKeys.coverageHighlighting := true

publishingSettings
integrationTestSettings
resolvers += Resolver.jcenterRepo

enablePlugins(PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin, SbtArtifactory)
