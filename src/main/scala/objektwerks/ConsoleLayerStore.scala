package objektwerks

import zio.{ZIO, Has, Task, ZLayer}

object ConsoleLayerStore {
  type ConsoleLayerStoreEnv = Has[ConsoleLayerStore.Service]

  trait Service {
    def store(message: Message): Task[Message]
  }

  val live: ZLayer[Any, Nothing, ConsoleLayerStoreEnv] = ZLayer.succeed {
    new Service {
      override def store(message: Message): Task[Message] = Task {
        println(s"[ConsoleLayerStore] stored: $message")
        message
      }
    }
  }

  def store(message: Message): ZIO[ConsoleLayerStoreEnv, Throwable, Message] = ZIO.accessM(_.get.store(message))
}