package objektwerks

import com.typesafe.config.ConfigFactory

import zio.{Has, Task, ZLayer}
import zio.config._
import zio.config.magnolia.DeriveConfigDescriptor.descriptor
import zio.config.typesafe.TypesafeConfigSource
import zio.macros.accessible

case class ConsoleConfig(question: String, response: String)

@accessible
object ConsoleConfig {
  val empty = ConsoleConfig("", "")

  type Config = Has[ConsoleConfig.Service]

  trait Service {
    def load(path: String): Task[ConsoleConfig]
  }

  val live: ZLayer[Any, Nothing, Config] = ZLayer.succeed {
    new Service {
      override def load(path: String): Task[ConsoleConfig] = Task {
        toConfig(path)
      }
    }
  }

  def toConfig(path: String): ConsoleConfig = {
    val conf = for {
      source <- TypesafeConfigSource.fromTypesafeConfig( ConfigFactory.load(path) )
      conf   <- read(descriptor[ConsoleConfig] from source)
    } yield conf
    conf.getOrElse(empty)
  }
}