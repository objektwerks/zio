package objektwerks

import com.typesafe.config.ConfigFactory

import zio.{Has, Layer, Task, ZLayer}
import zio.config._
import zio.config.magnolia.DeriveConfigDescriptor.descriptor
import zio.config.typesafe.{TypesafeConfig, TypesafeConfigSource}
import zio.macros.accessible

case class ConsoleConfig(question: String, response: String)

@accessible
object ConsoleConfig {
  val empty = ConsoleConfig("", "")

  type Config = Has[ConsoleConfig.Service]

  trait Service {
    def loadLayer(path: String): Task[Layer[ReadError[String], Has[ConsoleConfig]]]
  }

  val live: ZLayer[Any, Nothing, Config] = ZLayer.succeed {
    new Service {
      override def loadLayer(path: String): Task[Layer[ReadError[String], Has[ConsoleConfig]]] = Task {
        TypesafeConfig.fromTypesafeConfig( ConfigFactory.load(path), descriptor[ConsoleConfig] )
      }
    }
  }

  def load(path: String): Either[ReadError[String], ConsoleConfig] =
    TypesafeConfigSource.fromTypesafeConfig( ConfigFactory.load(path) ) match {
      case Right(source) => read(descriptor[ConsoleConfig] from source)
      case Left(error) => Left(error)
    }
}