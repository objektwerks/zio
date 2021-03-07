package objektwerks

import zio.{ZIO, Has, Task, ZLayer}

object ConsoleLayerService {
  type ConsoleLayerServiceEnv = Has[ConsoleLayerService.Service]

  trait Service {
    def print(message: Message): Task[Message]
  }
  
  val live: ZLayer[Any, Nothing, ConsoleLayerServiceEnv] = ZLayer.succeed {
    new Service {
      override def print(message: Message): Task[Message] = Task {
        println(s"[ConsoleLayerService] printed: $message")
        message
      }
    } 
  }

  def print(message: Message): ZIO[ConsoleLayerServiceEnv, Throwable, Message] = ZIO.accessM(_.get.print(message))
}