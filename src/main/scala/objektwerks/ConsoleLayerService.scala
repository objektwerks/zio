package objektwerks

import zio.{ZIO, Has, Task, ZLayer}

object ConsoleLayerService {
  type ConsoleLayerServiceEnv = Has[ConsoleLayerService.Service]

  trait Service {
    def notify(message: Message): Task[Message]
  }
  
  val live: ZLayer[Any, Nothing, ConsoleLayerServiceEnv] = ZLayer.succeed {
    new Service {
      override def notify(message: Message): Task[Message] = Task {
        println(s"[ConsoleLayerService] message: ${message.text}")
        message
      }
    } 
  }

  def notify(message: Message): ZIO[ConsoleLayerServiceEnv, Throwable, Message] = ZIO.accessM(_.get.notify(message))
}