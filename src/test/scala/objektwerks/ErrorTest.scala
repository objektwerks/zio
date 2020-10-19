package objektwerks

import zio.test.Assertion.isTrue
import zio.test._
import zio.{Task, ZIO}

object ErrorTest extends ZioTest {
  def spec: Spec[Environment, TestFailure[Nothing], TestSuccess] = suite("error.test")(
    test("error") {
      val fallback: Task[String] = file("build.sat") orElse file("build.sbt")
      assert(runtime.unsafeRun( fallback ).nonEmpty)(isTrue)

      val fold: Task[String] = file("build.sat").foldM(_ => file("build.sbt"), source => ZIO.succeed(source))
      assert(runtime.unsafeRun( fold ).nonEmpty)(isTrue)

      val catchall: Task[String] = file("build.sat").catchAll(_ => file("build.sbt"))
      assert(runtime.unsafeRun( catchall ).mkString.nonEmpty)(isTrue)
    }
  )
}