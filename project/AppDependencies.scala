import sbt._

object AppDependencies {

  private val playVersion = "play-30"
  private val bootstrapVersion = "8.6.0"

  val compile = Seq(
    "uk.gov.hmrc"                 %% s"bootstrap-backend-$playVersion" % bootstrapVersion,
    "com.github.java-json-tools"  % "json-schema-validator"            % "2.2.14",
  )

  val test = Seq(
    "uk.gov.hmrc"   %% s"bootstrap-test-$playVersion" % bootstrapVersion,
    "org.scalamock" %% "scalamock"                    % "5.2.0"
  ).map(_ % "test, it")

  val all: Seq[ModuleID] = compile ++ test
}
