package objektwerks

import zio.{Task, ZIO}

import scala.io.{BufferedSource, Codec, Source}

object Resources {
  def resource(file: String): Task[BufferedSource] = ZIO.effect(Source.fromFile(file, Codec.UTF8.name))

  def close(source: BufferedSource): Task[Unit] = ZIO.effect(source.close)

  def stringify(source: BufferedSource): Task[String] = ZIO.effect(source.mkString)
}