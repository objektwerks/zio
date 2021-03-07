package objektwerks

import zio.{Has, Task, ZIO, ZLayer}

object ConsolePrinterStoreService {
  import ConsolePrinter._
  import ConsoleStore._

  type PrinterStoreService = Has[ConsolePrinterStoreService.Service]

  class Service(service: ConsolePrinter.Service, store: ConsoleStore.Service) {
    def printAndStore(message: Message): Task[Message] = {
      for {
        m  <- service.print(message)
        mm <- store.store(m)
      } yield mm
    }
  }

  val live: ZLayer[PrintService with StoreService, Nothing, PrinterStoreService] =
    ZLayer
    .fromServices[ConsolePrinter.Service, ConsoleStore.Service, ConsolePrinterStoreService.Service]( 
      (service, store) => new Service(service, store)
    )

  def printAndStore(message: Message): ZIO[PrinterStoreService, Throwable, Message] = ZIO.accessM(_.get.printAndStore(message))
}