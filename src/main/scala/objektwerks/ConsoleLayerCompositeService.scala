package objektwerks

import zio.{Has, Task, ZIO, ZLayer}

object ConsoleLayerCompositeService {
  import ConsoleLayerService._
  import ConsoleLayerStore._

  type ConsoleLayerCompositeServiceEnv = Has[ConsoleLayerCompositeService.Service]

  class Service(service: ConsoleLayerService.Service, store: ConsoleLayerStore.Service) {
    def notifyAndStore(message: Message): Task[Message] = {
      for {
        m <- service.notify(message)
        _ <- store.store(m)
      } yield message
    }
  }

  val live: ZLayer[ConsoleLayerServiceEnv with ConsoleLayerStoreEnv, Nothing, ConsoleLayerCompositeServiceEnv] =
    ZLayer
    .fromServices[ConsoleLayerService.Service, ConsoleLayerStore.Service, ConsoleLayerCompositeService.Service]( (service, store) =>
      new Service(service, store)
    )

  def notifyAndStore(message: Message): ZIO[ConsoleLayerCompositeServiceEnv, Throwable, Message] = ZIO.accessM(_.get.notifyAndStore(message))
}