package objektwerks

import zio.test.Assertion.isTrue
import zio.test._
import zio.{Managed, Task, ZIO}

import scala.io.{BufferedSource, Codec, Source}

object ManagedTest extends ZioTest {
  def spec: Spec[Environment, TestFailure[Nothing], TestSuccess] = suite("managed.test")(
    test("managed") {
      def resource(file: String): Task[BufferedSource] = ZIO.effect(Source.fromFile(file, Codec.UTF8.name))
      def close(source: BufferedSource): Task[Unit] = ZIO.effect(source.close)
      def tostring(source: BufferedSource): Task[String] = ZIO.effect(source.mkString)

      val managed = Managed.make(resource("build.sbt"))(close(_).ignore)
      val task: Task[String] = managed.use { source => tostring(source) }
      assert(runtime.unsafeRun( task ).nonEmpty)(isTrue)
    }
  )
}