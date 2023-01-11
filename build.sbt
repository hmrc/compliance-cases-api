import play.core.PlayVersion
import play.sbt.PlayScala
import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings.integrationTestSettings
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings

val appName = "compliance-cases-api"

scalaVersion := "2.13.10"
majorVersion := 0
PlayKeys.playDefaultPort := 7052

lazy val microservice = Project(appName, file("."))
  .disablePlugins(JUnitXmlReportPlugin)
  .configs(IntegrationTest)


scalacOptions ++= Seq(
  "-Wconf:cat=unused-imports&src=html/.*:s",
  "-Wconf:src=routes/.*:s"
)

val bootstrapVersion = "7.9.0"

libraryDependencies  ++= Seq(
  "uk.gov.hmrc"                 %% "bootstrap-backend-play-28"% bootstrapVersion,
  "com.github.java-json-tools"  % "json-schema-validator"     % "2.2.14",
  "org.scalatest"               %% "scalatest"                % "3.2.9"                % "test,it",
  "uk.gov.hmrc"                 %% "bootstrap-test-play-28"   % bootstrapVersion        % Test,
  "org.pegdown"                 %  "pegdown"                  % "1.6.0"                 % "test, it",
  "org.scalatestplus.play"      %% "scalatestplus-play"       % "5.1.0"                 % "test, it",
  "org.scalamock"               %% "scalamock"                % "5.2.0"                 % "test",
  "com.github.tomakehurst"      % "wiremock-standalone"       % "3.0.0-beta-2"                % "test, it",
  "com.vladsch.flexmark"        % "flexmark-all"              % "0.35.10"                % "test, it"
)

ScoverageKeys.coverageExcludedFiles := "<empty>;Reverse.*;.*Routes.*;.*GuiceInjector;"
ScoverageKeys.coverageMinimum := 100
ScoverageKeys.coverageFailOnMinimum := true
ScoverageKeys.coverageHighlighting := true

publishingSettings
integrationTestSettings()
resolvers += Resolver.jcenterRepo

enablePlugins(PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin)
disablePlugins(JUnitXmlReportPlugin)
