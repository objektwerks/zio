package objektwerks

import zio.{Managed, Task, ZIO}

import scala.io.{BufferedSource, Codec, Source}

class ManagedResourceTest extends ZioTest{
  test("managed resource") {
    def resource(file: String): Task[BufferedSource] = ZIO.effect(Source.fromFile(file, Codec.UTF8.name))
    def close(source: BufferedSource): Task[Unit] = ZIO.effect(source.close)
    def tostring(source: BufferedSource): Task[String] = ZIO.effect(source.mkString)

    val managed = Managed.make(resource("build.sbt"))(close(_).catchAll(_ => Task.unit))
    val task: Task[String] = managed.use { source => tostring(source) }
    runtime.unsafeRun( task ).nonEmpty shouldBe true
  }
}