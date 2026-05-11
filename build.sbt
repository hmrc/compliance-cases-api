import play.sbt.PlayScala
import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings.integrationTestSettings

val appName = "compliance-cases-api"

scalaVersion := "3.7.4"
majorVersion := 0
PlayKeys.playDefaultPort := 7052

scalacOptions ++= Seq(
  "-Wconf:msg=unused import*:s",
  "-Wconf:src=routes/.*:s",
  "-Wconf:msg=Flag.*repeatedly:s",
  "-Wconf:msg=unused private member*:s"
)
libraryDependencies  ++= AppDependencies.all

ScoverageKeys.coverageExcludedPackages := Seq("<empty>","Reverse.*",".*Routes.*",".*GuiceInjector","$anon").mkString(",")
ScoverageKeys.coverageMinimumStmtTotal := 90
ScoverageKeys.coverageFailOnMinimum := true
ScoverageKeys.coverageHighlighting := true

Global / excludeLintKeys ++= Set(
  IntegrationTest / javaSource,
  IntegrationTest / scalaSource,
  IntegrationTest / semanticdbTargetRoot,
  IntegrationTest / sourceDirectories
)

integrationTestSettings()

enablePlugins(PlayScala, SbtDistributablesPlugin)
disablePlugins(JUnitXmlReportPlugin)

lazy val microservice = Project(appName, file("."))
  .disablePlugins(JUnitXmlReportPlugin)
  .configs(IntegrationTest)
