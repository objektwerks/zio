package objektwerks

import zio.{ZIO, Has, Task, ZLayer}

object ConsoleStore {
  type StoreService = Has[ConsoleStore.Service]

  trait Service {
    def store(message: Message): Task[Message]
  }

  val live: ZLayer[Any, Nothing, StoreService] = ZLayer.succeed {
    new Service {
      override def store(message: Message): Task[Message] = Task {
        println(s"[ConsoleLayerStore] stored: $message")
        message
      }
    }
  }

  def store(message: Message): ZIO[StoreService, Throwable, Message] = ZIO.accessM(_.get.store(message))
}