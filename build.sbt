name := "zio"
organization := "objektwerks"
version := "0.1-SNAPSHOT"
scalaVersion := "2.13.1"
libraryDependencies ++= {
  val zioVersion = "1.0.0-RC14"
  Seq(
    "dev.zio" %% "zio" % zioVersion,
    "dev.zio" %% "zio-streams" % zioVersion,
    "org.scalatest" %% "scalatest" % "3.0.8" % Test
  )
}