package zio

import org.scalatest.{FunSuite, Matchers}
import scalaz.zio.{DefaultRuntime, ZIO}

import scala.concurrent.Future
import scala.io.{Codec, Source}
import scala.util.Try

class ZioTest extends FunSuite with Matchers {
  test("effects") {
    val runtime = new DefaultRuntime {}

    runtime.unsafeRun( ZIO.succeed(33) ) shouldBe 33
    runtime.unsafeRun( ZIO.succeedLazy( List(1, 2, 3).sum ) ) shouldBe 6
    runtime.unsafeRun( ZIO.fromOption(Some(3)).map(_ * 2) ) shouldBe 6
    runtime.unsafeRun( ZIO.fromEither(Right(3)).map(_ * 2) ) shouldBe 6
    runtime.unsafeRun( ZIO.fromTry(Try(18 / 3)) ) shouldBe 6
    runtime.unsafeRun( ZIO.fromFunction((i: Int) => i * i).provide(3) ) shouldBe 9
    runtime.unsafeRun( ZIO.fromFuture { implicit ec => Future(3).map(_ * 3) } ) shouldBe 9
    runtime.unsafeRun( ZIO.effect( Source.fromFile("build.sbt", Codec.UTF8.name) ) ).mkString.nonEmpty shouldBe true
  }
}