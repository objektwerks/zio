package objektwerks

import zio.{ZIO, Has, Task, ZLayer}

object ConsoleStore {
  type Store = Has[ConsoleStore.Service]

  trait Service {
    def store(message: Message): Task[Message]
  }

  val live: ZLayer[Any, Nothing, Store] = ZLayer.succeed {
    new Service {
      override def store(message: Message): Task[Message] = Task {
        println(s"[Store] stored: $message")
        message
      }
    }
  }

  def store(message: Message): ZIO[Store, Throwable, Message] = ZIO.accessM(_.get.store(message))
}