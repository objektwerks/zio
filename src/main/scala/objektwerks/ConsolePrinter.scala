package objektwerks

import zio.{ZIO, Has, Task, ZLayer}

object ConsolePrinter {
  type PrintService = Has[ConsolePrinter.Service]

  trait Service {
    def print(message: Message): Task[Message]
  }
  
  val live: ZLayer[Any, Nothing, PrintService] = ZLayer.succeed {
    new Service {
      override def print(message: Message): Task[Message] = Task {
        println(s"[ConsoleLayerService] printed: $message")
        message
      }
    } 
  }

  def print(message: Message): ZIO[PrintService, Throwable, Message] = ZIO.accessM(_.get.print(message))
}