package objektwerks

import zio.{Has, Task, ZIO, ZLayer}

object ConsolePrintStoreService {
  import ConsolePrinter._
  import ConsoleStore._

  type PrintStoreService = Has[ConsolePrintStoreService.Service]

  class Service(service: ConsolePrinter.Service, store: ConsoleStore.Service) {
    def printAndStore(message: Message): Task[Message] = {
      for {
        m  <- service.print(message)
        mm <- store.store(m)
      } yield mm
    }
  }

  val live: ZLayer[PrintService with StoreService, Nothing, PrintStoreService] =
    ZLayer
    .fromServices[ConsolePrinter.Service, ConsoleStore.Service, ConsolePrintStoreService.Service]( 
      (service, store) => new Service(service, store)
    )

  def printAndStore(message: Message): ZIO[PrintStoreService, Throwable, Message] = ZIO.accessM(_.get.printAndStore(message))
}