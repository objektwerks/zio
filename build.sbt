name := "zio"
organization := "objektwerks"
version := "0.1-SNAPSHOT"
scalaVersion := "2.13.5"
libraryDependencies ++= {
  val zioVersion = "1.0.7"
  val zioConfigVersion = "1.0.4"
  Seq(
    "dev.zio" %% "zio" % zioVersion,
    "dev.zio" %% "zio-streams" % zioVersion,
    "dev.zio" %% "zio-macros" % zioVersion,
    "dev.zio" %% "zio-config" % zioConfigVersion,
    "dev.zio" %% "zio-config-magnolia" % zioConfigVersion,
    "dev.zio" %% "zio-config-typesafe" % zioConfigVersion,
    "dev.zio" %% "zio-logging" % "0.5.8",
    "dev.zio" %% "zio-json" % "0.1.4",
    "io.d11" %% "zhttp" % "1.0.0.0-RC16",
    "dev.zio" %% "zio-test" % zioVersion % Test,
    "dev.zio" %% "zio-test-sbt" % zioVersion % Test
  )
}
scalacOptions += "-Ymacro-annotations"
parallelExecution := false
testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")