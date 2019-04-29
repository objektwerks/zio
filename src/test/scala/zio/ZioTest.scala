package zio

import org.scalatest.{FunSuite, Matchers}
import scalaz.zio.{DefaultRuntime, FiberLocal, Managed, Queue, Ref, Schedule, Task, ZIO}

import scala.concurrent.Future
import scala.io.{BufferedSource, Codec, Source}
import scala.util.Try

class ZioTest extends FunSuite with Matchers {
  val runtime = new DefaultRuntime {}

  def file(file: String): Task[String] = {
    def close(source: BufferedSource): Task[Unit] = ZIO.effect(source.close)

    def read(source: BufferedSource): Task[String] = ZIO.effect(source.mkString)

    def open(file: String): Task[String] = ZIO.effect(Source.fromFile(file, Codec.UTF8.name))
      .bracket(close(_).catchAll(_ => Task.unit))(source => read(source))

    open(file)
  }

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
    val fallback = file("build.sat") orElse file("build.sbt")
    runtime.unsafeRun( fallback ).nonEmpty shouldBe true

    val fold: Task[String] = file("build.sat").foldM(_ => file("build.sbt"), source => ZIO.succeed(source))
    runtime.unsafeRun( fold ).nonEmpty shouldBe true

    val catchall: Task[String] = file("build.sat").catchAll(_ => file("build.sbt"))
    runtime.unsafeRun( catchall ).mkString.nonEmpty shouldBe true

    val retryOrElse = file("build.sat").retryOrElse( Schedule.once, (_: Throwable, _: Unit) => file("build.sbt"))
    runtime.unsafeRun( retryOrElse ).mkString.nonEmpty shouldBe true
  }

  test("fibers") {
    val fileContent = for {
      fiber <- file("build.sbt").fork
      source <- fiber.join
    } yield source.mkString
    runtime.unsafeRun( fileContent ).nonEmpty shouldBe true

    val helloWorld = for {
      helloFiber <- ZIO.succeed("Hello, ").fork
      worldFiber <- ZIO.succeed("world!").fork
      fiber = helloFiber zip worldFiber
      tuple <- fiber.join
    } yield tuple._1 + tuple._2
    runtime.unsafeRun( helloWorld ) shouldBe "Hello, world!"
  }

  test("fiber local") {
    val fiberLocalInt = for {
      local <- FiberLocal.make[Int]
      _     <- local.set(3)
      value <- local.get
      _     <- local.empty
    } yield value
    runtime.unsafeRun( fiberLocalInt ).get shouldBe 3
  }

  test("ref") {
    val refInt = for {
      ref     <- Ref.make(3)
      initial <- ref.get
      _       <- ref.set(initial * 3)
      result  <- ref.get
    } yield result
    runtime.unsafeRun( refInt ) shouldBe 9
  }

  test("managed resource") {
    def resource(file: String): Task[BufferedSource] = ZIO.effect(Source.fromFile(file, Codec.UTF8.name))
    def close(source: BufferedSource): Task[Unit] = ZIO.effect(source.close)
    def tostring(source: BufferedSource): Task[String] = ZIO.effect(source.mkString)

    val managed = Managed.make(resource("build.sbt"))(close(_).catchAll(_ => Task.unit))
    val task: Task[String] = managed.use { source => tostring(source) }
    runtime.unsafeRun( task ).nonEmpty shouldBe true
  }

  test("queue") {
    val queueInt = for {
      queue  <- Queue.bounded[Int](3)
      _      <- queue.offer(3)
      result <- queue.take
    } yield result
    runtime.unsafeRun( queueInt ) shouldBe 3
  }
}