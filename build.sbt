import play.sbt.PlayScala
import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings

val appName = "compliance-cases-api"

ThisBuild / scalaVersion := "3.7.4"
ThisBuild / majorVersion := 0
PlayKeys.playDefaultPort := 7052

scalacOptions ++= Seq(
  "-Wconf:msg=unused import*:s",
  "-Wconf:src=routes/.*:s",
  "-Wconf:msg=Flag.*repeatedly:s",
  "-Wconf:msg=unused private member*:s"
)

ScoverageKeys.coverageExcludedPackages := Seq("<empty>","Reverse.*",".*Routes.*",".*GuiceInjector","$anon").mkString(",")
ScoverageKeys.coverageMinimumStmtTotal := 90
ScoverageKeys.coverageFailOnMinimum := true
ScoverageKeys.coverageHighlighting := true

lazy val microservice = Project(appName, file("."))
  .enablePlugins(PlayScala, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(
    libraryDependencies ++= AppDependencies.all
  )

lazy val it = (project in file("it"))
  .enablePlugins(PlayScala)
  .dependsOn(microservice % "compile->compile;test->compile")
  .settings(DefaultBuildSettings.itSettings())
  .settings(
    libraryDependencies ++= AppDependencies.test,
    Test / scalaSource := baseDirectory.value / "test",
    Test / scalacOptions --= Seq("-deprecation", "-unchecked", "-encoding", "UTF-8")
  )

addCommandAlias("testAll", "; test ; it/test")