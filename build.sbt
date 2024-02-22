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

val compileDependencies = Seq(
  "uk.gov.hmrc"                 %% s"bootstrap-backend-$playVersion"    % bootstrapVersion,
  "com.github.java-json-tools"  % "json-schema-validator"               % "2.2.14",
)

val testDependencies = Seq(
  "uk.gov.hmrc"                 %% s"bootstrap-test-$playVersion"       % bootstrapVersion,
  "org.scalamock"               %% "scalamock"                          % "5.2.0"
).map(_ % "test, it")

libraryDependencies  ++= compileDependencies ++ testDependencies

ScoverageKeys.coverageExcludedFiles := "<empty>;Reverse.*;.*Routes.*;.*GuiceInjector;"
ScoverageKeys.coverageMinimumStmtTotal := 100
ScoverageKeys.coverageFailOnMinimum := true
ScoverageKeys.coverageHighlighting := true

integrationTestSettings()
resolvers += Resolver.jcenterRepo

enablePlugins(PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin)
disablePlugins(JUnitXmlReportPlugin)
