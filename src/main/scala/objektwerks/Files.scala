package objektwerks

import zio.{Task, ZIO}

import scala.io.{BufferedSource, Codec, Source}

object Files {
  def close(source: BufferedSource): Task[Unit] = ZIO.effect(source.close)

  def read(source: BufferedSource): Task[String] = ZIO.effect(source.mkString)

  def open(file: String): Task[String] =
    ZIO
      .effect(Source.fromFile(file, Codec.UTF8.name))
      .bracket(close(_).ignore)(source => read(source))

  def file(file: String): Task[String] = open(file)
}