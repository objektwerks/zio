package zio

import org.scalatest.{FunSuite, Matchers}
import scalaz.zio.{DefaultRuntime, Schedule, Task, ZIO}

import scala.concurrent.Future
import scala.io.{BufferedSource, Codec, Source}
import scala.util.Try

class ZioTest extends FunSuite with Matchers {
  val utf8 = Codec.UTF8.name
  val runtime = new DefaultRuntime {}

  test("effects") {
    runtime.unsafeRun( ZIO.fail("fail").mapError(error => new Exception(error)).either ).left.toOption.nonEmpty shouldBe true

    runtime.unsafeRun( ZIO.succeed(33) ) shouldBe 33
    runtime.unsafeRun( ZIO.succeedLazy( List(1, 2, 3).sum ) ) shouldBe 6

    runtime.unsafeRun( ZIO.fromOption(Some(3)).map(_ * 2) ) shouldBe 6
    runtime.unsafeRun( ZIO.fromEither(Right(3)).map(_ * 2) ) shouldBe 6
    runtime.unsafeRun( ZIO.fromTry(Try(18 / 3)) ) shouldBe 6
    runtime.unsafeRun( ZIO.fromFunction((i: Int) => i * i).provide(3) ) shouldBe 9
    runtime.unsafeRun( ZIO.fromFuture { implicit ec => Future(3).map(_ * 3) } ) shouldBe 9
  }

  test("errors") {
    val invalidFileEffect = ZIO.effect( Source.fromFile("build.sat", utf8) )
    val validFileEffect = ZIO.effect( Source.fromFile("build.sbt", utf8) )

    val fallback = invalidFileEffect orElse validFileEffect
    val fold: Task[BufferedSource] = invalidFileEffect.foldM(_ => validFileEffect, source => ZIO.succeed(source))
    val catchall: Task[BufferedSource] = invalidFileEffect.catchAll(_ => validFileEffect)
    val retryOrElse = invalidFileEffect.retryOrElse( Schedule.once, (_: Throwable,_: Unit) => validFileEffect)

    runtime.unsafeRun( fallback ).mkString.nonEmpty shouldBe true
    runtime.unsafeRun( fold ).mkString.nonEmpty shouldBe true
    runtime.unsafeRun( catchall ).mkString.nonEmpty shouldBe true
    runtime.unsafeRun( retryOrElse ).mkString.nonEmpty shouldBe true
  }

  test("fibers") {

  }
}