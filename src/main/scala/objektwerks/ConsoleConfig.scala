package objektwerks

import java.io.File

import zio.config.magnolia.DeriveConfigDescriptor.descriptor
import zio.config.typesafe.TypesafeConfig

case class ConsoleConfig(question: String, response: String)

object ConsoleConfig {
  val derived = descriptor[ConsoleConfig]
  val live = TypesafeConfig.fromHoconFile(new File("/console.conf"), derived)
}