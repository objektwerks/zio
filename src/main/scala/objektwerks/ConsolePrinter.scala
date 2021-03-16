package objektwerks

import zio.{ZIO, Has, Task, ZLayer}

object ConsolePrinter {
  type Printer = Has[ConsolePrinter.Service]

  trait Service {
    def print(message: Message): Task[Message]
  }
  
  val live: ZLayer[Any, Nothing, Printer] = ZLayer.succeed {
    (message: Message) => Task {
      println(s"[Printer] printed: $message")
      message
    }
  }

  def print(message: Message): ZIO[Printer, Throwable, Message] = ZIO.accessM(_.get.print(message))
}