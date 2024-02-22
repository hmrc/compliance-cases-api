import play.core.PlayVersion
import play.sbt.PlayScala
import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings.integrationTestSettings
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings

val appName = "compliance-cases-api"

scalaVersion := "2.13.12"
majorVersion := 0
PlayKeys.playDefaultPort := 7052

lazy val microservice = Project(appName, file("."))
  .disablePlugins(JUnitXmlReportPlugin)
  .configs(IntegrationTest)


scalacOptions ++= Seq(
  "-Wconf:cat=unused-imports&src=html/.*:s",
  "-Wconf:src=routes/.*:s"
)

val bootstrapVersion = "8.4.0"
val playVersion = "play-30"

libraryDependencies  ++= Seq(
  "uk.gov.hmrc"                 %% s"bootstrap-backend-$playVersion"    % bootstrapVersion,
  "com.github.java-json-tools"  % "json-schema-validator"               % "2.2.14",
  "org.scalatest"               %% "scalatest"                          % "3.2.17"                 % "test,it",
  "uk.gov.hmrc"                 %% s"bootstrap-test-$playVersion"       % bootstrapVersion        % Test,
  "org.pegdown"                 %  "pegdown"                            % "1.6.0"                 % "test, it",
  "org.scalatestplus.play"      %% "scalatestplus-play"                 % "7.0.1"                 % "test, it",
  "org.scalamock"               %% "scalamock"                          % "5.2.0"                 % "test",
  "org.wiremock"                % "wiremock-standalone"                 % "3.4.1"                 % "test, it",
  "com.vladsch.flexmark"        % "flexmark-all"                        % "0.64.8"               % "test, it"
)

ScoverageKeys.coverageExcludedFiles := "<empty>;Reverse.*;.*Routes.*;.*GuiceInjector;"
ScoverageKeys.coverageMinimumStmtTotal := 100
ScoverageKeys.coverageFailOnMinimum := true
ScoverageKeys.coverageHighlighting := true

publishingSettings
integrationTestSettings()
resolvers += Resolver.jcenterRepo

enablePlugins(PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin)
disablePlugins(JUnitXmlReportPlugin)
