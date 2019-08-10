package objektwerks

import zio.{Schedule, Task, ZIO}

class ErrorsTest extends ZioTest {
  test("errors") {
    val fallback = file("build.sat") orElse file("build.sbt")
    runtime.unsafeRun( fallback ).nonEmpty shouldBe true

    val fold: Task[String] = file("build.sat").foldM(_ => file("build.sbt"), source => ZIO.succeed(source))
    runtime.unsafeRun( fold ).nonEmpty shouldBe true

    val catchall: Task[String] = file("build.sat").catchAll(_ => file("build.sbt"))
    runtime.unsafeRun( catchall ).mkString.nonEmpty shouldBe true

    val retryOrElse = file("build.sat").retryOrElse( Schedule.once, (_: Throwable, _: Unit) => file("build.sbt"))
    runtime.unsafeRun( retryOrElse ).mkString.nonEmpty shouldBe true
  }
}