import play.sbt.PlayScala
import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings.integrationTestSettings

val appName = "compliance-cases-api"

scalaVersion := "2.13.12"
majorVersion := 0
PlayKeys.playDefaultPort := 7052

scalacOptions ++= Seq(
  "-Wconf:cat=unused-imports&src=html/.*:s",
  "-Wconf:src=routes/.*:s"
)
libraryDependencies  ++= AppDependencies.all

ScoverageKeys.coverageExcludedFiles := "<empty>;Reverse.*;.*Routes.*;.*GuiceInjector;"
ScoverageKeys.coverageMinimumStmtTotal := 92
ScoverageKeys.coverageFailOnMinimum := true
ScoverageKeys.coverageHighlighting := true

integrationTestSettings()
resolvers += Resolver.jcenterRepo

enablePlugins(PlayScala, SbtDistributablesPlugin)
disablePlugins(JUnitXmlReportPlugin)

lazy val microservice = Project(appName, file("."))
  .disablePlugins(JUnitXmlReportPlugin)
  .configs(IntegrationTest)
