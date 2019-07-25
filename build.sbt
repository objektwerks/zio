name := "zio"
organization := "objektwerks"
version := "0.1-SNAPSHOT"
scalaVersion := "2.12.8"
libraryDependencies ++= {
  Seq(
    "org.scalaz" %% "scalaz-zio" % "1.0-RC5",
    "org.scalatest" %% "scalatest" % "3.0.8" % Test
  )
}