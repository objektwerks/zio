package objektwerks

import zio.test._
import zio.{Runtime, Task, ZIO}

import scala.io.{BufferedSource, Codec, Source}

trait ZioTest extends DefaultRunnableSpec {
  protected val runtime: Runtime[zio.ZEnv] = Runtime.default

  def file(file: String): Task[String] = {
    def close(source: BufferedSource): Task[Unit] = ZIO.effect(source.close)
    def read(source: BufferedSource): Task[String] = ZIO.effect(source.mkString)
    def open(file: String): Task[String] = ZIO
      .effect(Source.fromFile(file, Codec.UTF8.name))
      .bracket(close(_).ignore)(source => read(source))
    open(file)
  }
}