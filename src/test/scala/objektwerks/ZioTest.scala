package objektwerks

import zio.{FiberRef, Managed, Queue, Ref, Runtime, Schedule, Task, ZIO}
import zio.test._
import zio.test.Assertion._

import scala.concurrent.Future
import scala.io.{BufferedSource, Codec, Source}
import scala.util.Try

object ZioTest extends DefaultRunnableSpec {
  val runtime = Runtime.default

  def spec:Spec[Environment, TestFailure[Nothing], TestSuccess] = suite("zio.spec.test")(
    test("effects") {
      assert(runtime.unsafeRun( ZIO.fail("fail").mapError(error => new Exception(error)).either ).isLeft)(equalTo(true))
      assert(runtime.unsafeRun( ZIO.succeed(33) ))(equalTo(33))
      assert(runtime.unsafeRun( ZIO.effectTotal( List(1, 2, 3).sum )))(equalTo(6))

      assert(runtime.unsafeRun( ZIO.fromOption(Some(3)).map(_ * 2) ))(equalTo(6))
      assert(runtime.unsafeRun( ZIO.fromEither(Right(3)).map(_ * 2) ))(equalTo(6))
      assert(runtime.unsafeRun( ZIO.fromTry(Try(18 / 3)) ))(equalTo(6))
      assert(runtime.unsafeRun( ZIO.fromFunction((i: Int) => i * i).provide(3) ))(equalTo(9))
      assert(runtime.unsafeRun( ZIO.fromFuture { implicit ec => Future(3).map(_ * 3) } ))(equalTo(9))
    },

    test("errors") {
      val fallback: Task[String] = file("build.sat") orElse file("build.sbt")
      assert(runtime.unsafeRun( fallback ).nonEmpty)(equalTo(true))

      val fold: Task[String] = file("build.sat").foldM(_ => file("build.sbt"), source => ZIO.succeed(source))
      assert(runtime.unsafeRun( fold ).nonEmpty)(equalTo(true))

      val catchall: Task[String] = file("build.sat").catchAll(_ => file("build.sbt"))
      assert(runtime.unsafeRun( catchall ).mkString.nonEmpty)(equalTo(true))

      val retryOrElse: Task[String] = file("build.sat").retryOrElse( Schedule.once, (_: Throwable, _: Unit) => file("build.sbt"))
      assert(runtime.unsafeRun( retryOrElse ).mkString.nonEmpty)(equalTo(true))
    },

    test("fiber ref") {
      val fiberRefInt = for {
        fiberRef <- FiberRef.make[Int](0)
        _        <- fiberRef.set(3)
        value    <- fiberRef.get
      } yield value
      assert(runtime.unsafeRun( fiberRefInt ))(equalTo(3))
    },

    test("fiber") {
      val fileContent = for {
        fiber <- file("build.sbt").fork
        source <- fiber.join
      } yield source.mkString
      assert(runtime.unsafeRun( fileContent ).nonEmpty)(equalTo(true))

      val helloWorld = for {
        helloFiber <- ZIO.succeed("Hello, ").fork
        worldFiber <- ZIO.succeed("world!").fork
        fiber = helloFiber zip worldFiber
        tuple <- fiber.join
      } yield {
        val (hello, world) = tuple
        hello + world
      }
      assert(runtime.unsafeRun( helloWorld ))(equalTo("Hello, world!"))
    },

    test("managed resource") {
      def resource(file: String): Task[BufferedSource] = ZIO.effect(Source.fromFile(file, Codec.UTF8.name))
      def close(source: BufferedSource): Task[Unit] = ZIO.effect(source.close)
      def tostring(source: BufferedSource): Task[String] = ZIO.effect(source.mkString)

      val managed = Managed.make(resource("build.sbt"))(close(_).ignore)
      val task: Task[String] = managed.use { source => tostring(source) }
      assert(runtime.unsafeRun( task ).nonEmpty)(equalTo(true))
    },

    test("queue") {
      val queueInt = for {
        queue  <- Queue.bounded[Int](3)
        _      <- queue.offer(3)
        result <- queue.take
      } yield result
      assert(runtime.unsafeRun( queueInt ))(equalTo(3))
    },

    test("ref") {
      val refInt = for {
        ref     <- Ref.make(3)
        initial <- ref.get
        _       <- ref.set(initial * 3)
        result  <- ref.get
      } yield result
      assert(runtime.unsafeRun( refInt ))(equalTo(9))
    }
  )

  def file(file: String): Task[String] = {
    def close(source: BufferedSource): Task[Unit] = ZIO.effect(source.close)
    def read(source: BufferedSource): Task[String] = ZIO.effect(source.mkString)
    def open(file: String): Task[String] = ZIO
      .effect(Source.fromFile(file, Codec.UTF8.name))
      .bracket(close(_).ignore)(source => read(source))
    open(file)
  }
}