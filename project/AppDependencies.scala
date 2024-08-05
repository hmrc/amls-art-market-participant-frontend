import sbt._

object AppDependencies {

  private val bootstrapVersion = "9.1.0"

  val compile = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc" %% "play-frontend-hmrc-play-30"            % "10.5.0",
    "uk.gov.hmrc" %% "play-conditional-form-mapping-play-30" % "3.1.0",
    "uk.gov.hmrc" %% "bootstrap-frontend-play-30"            % bootstrapVersion
  )

  val test = Seq(
    "uk.gov.hmrc"         %% "bootstrap-test-play-30"  % bootstrapVersion,
    "org.scalatestplus"   %% "scalacheck-1-17"         % "3.2.17.0"
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test
}
