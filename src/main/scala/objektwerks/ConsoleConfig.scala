package objektwerks

import java.io.File

import zio.config._
import zio.config.magnolia.DeriveConfigDescriptor.descriptor
import zio.config.typesafe.TypesafeConfigSource

case class ConsoleConfig(question: String, response: String)

object ConsoleConfig {
  val empty = ConsoleConfig("", "")
  val desc = descriptor[ConsoleConfig]

  def load(path: String): Either[ReadError[String], ConsoleConfig] =
    TypesafeConfigSource.fromHoconFile( new File(path) ) match {
      case Right(source) => read(descriptor[ConsoleConfig] from source)
      case Left(error) => Left(error)
    }
}