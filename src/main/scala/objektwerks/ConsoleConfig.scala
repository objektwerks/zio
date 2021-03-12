package objektwerks

import java.io.File

import zio.config.magnolia.DeriveConfigDescriptor.descriptor
import zio.config.typesafe.TypesafeConfig._

case class ConsoleConfig(question: String, response: String)

object ConsoleConfig {
  def read(path: String) = fromHoconFile(new File(path), descriptor[ConsoleConfig])
}