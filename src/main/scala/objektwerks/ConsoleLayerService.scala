package objektwerks

import zio.{ZIO, Has, Task, ZLayer}

case class Message(text: String)

object ConsoleLayerService {
  type ConsoleLayerServiceEnv = Has[ConsoleLayerService.Service]

  trait Service {
    def notify(message: Message): Task[Unit]
  }
  
  val live: ZLayer[Any, Nothing, ConsoleLayerServiceEnv] = ZLayer.succeed( new Service {
    override def notify(message: Message): Task[Unit] =
      Task {
        println(s"[ConsoleLayerService] message: ${message.text}")
      }
  } )

  def notify(message: Message): ZIO[ConsoleLayerServiceEnv, Throwable, Unit] = ZIO.accessM(_.get.notify(message))
}