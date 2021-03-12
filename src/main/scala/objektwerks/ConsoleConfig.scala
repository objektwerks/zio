package objektwerks

import com.typesafe.config.ConfigFactory

import zio.config._
import zio.config.magnolia.DeriveConfigDescriptor.descriptor
import zio.config.typesafe.TypesafeConfigSource

case class ConsoleConfig(question: String, response: String)

object ConsoleConfig {
  val empty = ConsoleConfig("", "")

  def load(path: String): Either[ReadError[String], ConsoleConfig] =
    TypesafeConfigSource.fromTypesafeConfig( ConfigFactory.load(path) ) match {
      case Right(source) => read(descriptor[ConsoleConfig] from source)
      case Left(error) => Left(error)
    }
}