import play.sbt.PlayScala
import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings.integrationTestSettings

val appName = "compliance-cases-api"

scalaVersion := "3.3.4"
majorVersion := 0
PlayKeys.playDefaultPort := 7052

scalacOptions ++= Seq(
  "-Wconf:msg=unused import*:s",
  "-Wconf:msg=routes/.*:s",
  "-Wconf:msg=Flag.*repeatedly:s",
  "-Wconf:msg=unused private member*:s"
)
libraryDependencies  ++= AppDependencies.all

//ScoverageKeys.coverageExcludedFiles := "<empty>;Reverse.*;.*Routes.*;.*GuiceInjector;"
ScoverageKeys.coverageExcludedPackages := Seq("<empty>","Reverse.*",".*Routes.*",".*GuiceInjector","$anon").mkString(",")
ScoverageKeys.coverageMinimumStmtTotal := 80
ScoverageKeys.coverageFailOnMinimum := true
ScoverageKeys.coverageHighlighting := true

integrationTestSettings()
resolvers += Resolver.jcenterRepo

enablePlugins(PlayScala, SbtDistributablesPlugin)
disablePlugins(JUnitXmlReportPlugin)

lazy val microservice = Project(appName, file("."))
  .disablePlugins(JUnitXmlReportPlugin)
  .configs(IntegrationTest)
