name := "zio"
organization := "objektwerks"
version := "0.1-SNAPSHOT"
scalaVersion := "2.12.8"
libraryDependencies ++= {
  Seq(
    "org.scalaz" %% "scalaz-zio" % "1.0-RC3",
    "org.scalatest" % "scalatest_2.12" % "3.0.5" % Test
  )
}