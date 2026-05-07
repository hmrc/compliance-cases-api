import sbt._

object AppDependencies {

  private val playVersion = "play-30"
  private val bootstrapVersion = "10.7.0"

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"                 %% s"bootstrap-backend-$playVersion" % bootstrapVersion,
    "com.networknt"          % "json-schema-validator"            % "2.0.1" exclude ("com.fasterxml.jackson.core", "jackson-databind"),
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"   %% s"bootstrap-test-$playVersion" % bootstrapVersion,
    "org.scalamock" %% "scalamock"                    % "7.5.2"
  ).map(_ % "test,it")

  val all: Seq[ModuleID] = compile ++ test
}
