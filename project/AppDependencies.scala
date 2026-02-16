import sbt._

object AppDependencies {

  private val bootstrapVersion = "10.5.0"

  val compile = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc" %% "play-frontend-hmrc-play-30"            % "12.31.0",
    "uk.gov.hmrc" %% "play-conditional-form-mapping-play-30" % "3.4.0",
    "uk.gov.hmrc" %% "bootstrap-frontend-play-30"            % bootstrapVersion exclude("org.apache.commons", "commons-lang3"),
    "org.apache.commons"   % "commons-lang3"               % "3.18.0",
    "ch.qos.logback"       % "logback-core"                % "1.5.27",
    "ch.qos.logback"       % "logback-classic"             % "1.5.27",
    "at.yawk.lz4"          %  "lz4-java"                   % "1.10.3"
  )

  val test = Seq(
    "uk.gov.hmrc"         %% "bootstrap-test-play-30"  % bootstrapVersion,
    "org.scalatestplus"   %% "scalacheck-1-17"         % "3.2.18.0"
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test
}
