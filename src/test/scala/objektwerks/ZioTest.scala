package objektwerks

import org.scalatest.{FunSuite, Matchers}
import zio.{Runtime, Task, ZIO}

import scala.io.{BufferedSource, Codec, Source}

trait ZioTest extends FunSuite with Matchers {
  val runtime = Runtime.default

  def file(file: String): Task[String] = {
    def close(source: BufferedSource): Task[Unit] = ZIO.effect(source.close)

    def read(source: BufferedSource): Task[String] = ZIO.effect(source.mkString)

    def open(file: String): Task[String] = ZIO
      .effect(Source.fromFile(file, Codec.UTF8.name))
      .bracket(close(_).ignore)(source => read(source))

    open(file)
  }
}