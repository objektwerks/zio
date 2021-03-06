package objektwerks.console

import com.typesafe.config.ConfigFactory
import zio.config._
import zio.config.magnolia.DeriveConfigDescriptor.descriptor
import zio.config.typesafe.TypesafeConfigSource
import zio.{Has, Task, ZIO, ZLayer}

case class ConsoleConfig(question: String, response: String)

object ConsoleConfig {
  type Config = Has[ConsoleConfig.Service]

  trait Service {
    def load(path: String): Task[ConsoleConfig]
  }

  val live: ZLayer[Any, Nothing, Config] = ZLayer.succeed {
    (path: String) => Task {
      (for {
        source <- TypesafeConfigSource.fromTypesafeConfig(ConfigFactory.load(path))
        conf <- read(descriptor[ConsoleConfig] from source)
      } yield conf).getOrElse(ConsoleConfig("", ""))
    }
  }

  def load(path: String): ZIO[Config, Throwable, ConsoleConfig] = ZIO.accessM(_.get.load(path))
}