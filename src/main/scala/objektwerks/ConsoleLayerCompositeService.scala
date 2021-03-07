package objektwerks

import zio.{Has, Task, ZIO, ZLayer}

object ConsoleLayerCompositeService {
  import ConsoleLayerService._
  import ConsoleLayerStore._

  type ConsoleLayerCompositeServiceEnv = Has[ConsoleLayerCompositeService.Service]

  class Service(service: ConsoleLayerService.Service, store: ConsoleLayerStore.Service) {
    def printAndStore(message: Message): Task[Message] = {
      for {
        m <- service.print(message)
        _ <- store.store(m)
      } yield m
    }
  }

  val live: ZLayer[ConsoleLayerServiceEnv with ConsoleLayerStoreEnv, Nothing, ConsoleLayerCompositeServiceEnv] =
    ZLayer
    .fromServices[ConsoleLayerService.Service, ConsoleLayerStore.Service, ConsoleLayerCompositeService.Service]( 
      (service, store) => new Service(service, store)
    )

  def printAndStore(message: Message): ZIO[ConsoleLayerCompositeServiceEnv, Throwable, Message] = ZIO.accessM(_.get.printAndStore(message))
}