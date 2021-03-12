package objektwerks

import com.typesafe.config.ConfigFactory

import zio.{Has, Task, ZLayer, ZIO}
import zio.config._
import zio.config.magnolia.DeriveConfigDescriptor.descriptor
import zio.config.typesafe.TypesafeConfigSource

case class ConsoleConfig(question: String, response: String)

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

  def load(path: String): ZIO[Config, Throwable, ConsoleConfig] = ZIO.accessM(_.get.load(path))

  def toConfig(path: String): ConsoleConfig = {
    val conf = for {
      source <- TypesafeConfigSource.fromTypesafeConfig( ConfigFactory.load(path) )
      conf   <- read(descriptor[ConsoleConfig] from source)
    } yield conf
    conf.getOrElse(empty)
  }
}