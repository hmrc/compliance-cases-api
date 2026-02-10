import play.sbt.PlayScala
import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings

val appName = "compliance-cases-api"

ThisBuild / majorVersion := 0
ThisBuild / scalaVersion := "3.7.3"

ScoverageKeys.coverageExcludedPackages := Seq("<empty>","Reverse.*",".*Routes.*",".*GuiceInjector","$anon").mkString(",")
ScoverageKeys.coverageMinimumStmtTotal := 91
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
  .enablePlugins(PlayScala, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(
    libraryDependencies ++= AppDependencies.all,
    // https://www.scala-lang.org/2021/01/12/configuring-and-suppressing-warnings.html
    // suppress warnings in generated routes files
    scalacOptions += s"-Wconf:msg=unused import:s,msg=unused explicit parameter:s,src=.*[\\\\/]routes[\\\\/].*:s",
    Compile / scalacOptions --= Seq("-deprecation","-unchecked","-encoding","UTF-8"),
    Test    / scalacOptions --= Seq("-deprecation","-unchecked","-encoding","UTF-8")
  )
  .settings(
    Compile / unmanagedResourceDirectories += baseDirectory.value / "resources",
  )
  .settings(PlayKeys.playDefaultPort := 7052)
addCommandAlias("testAll", "; test ; it/test")

lazy val it = (project in file("it"))
  .enablePlugins(PlayScala)
  .dependsOn(microservice % "compile->compile,test;test->compile,test")
  .settings(DefaultBuildSettings.itSettings())
  .settings(libraryDependencies ++= AppDependencies.test,
    Test    / scalacOptions --= Seq("-deprecation","-unchecked","-encoding","UTF-8")
  )
  .settings( Test / scalaSource := baseDirectory.value / "scala")