package objektwerks

import zio.{ZIO, Has, Task, ZLayer}

object ConsoleLayerStore {
  type ConsoleLayerStoreEnv = Has[ConsoleLayerStore.Service]

  trait Service {
    def store(message: Message): Task[Unit]
  }

  val live: ZLayer[Any, Nothing, ConsoleLayerStoreEnv] = ZLayer.succeed {
    new Service {
      override def store(message: Message): Task[Unit] = Task {
        println(s"[ConsoleLayerStore] stored: $message")
      }
    }
  }

  def store(message: Message): ZIO[ConsoleLayerStoreEnv, Throwable, Unit] = ZIO.accessM(_.get.store(message))
}