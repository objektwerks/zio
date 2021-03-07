package objektwerks

import zio.{Has, Task, ZIO, ZLayer}

object ConsolePrinterStore {
  import ConsolePrinter._
  import ConsoleStore._

  type PrinterStoreService = Has[ConsolePrinterStore.Service]

  class Service(printer: ConsolePrinter.Service, store: ConsoleStore.Service) {
    def printAndStore(message: Message): Task[Message] = {
      for {
        m  <- printer.print(message)
        mm <- store.store(m)
      } yield mm
    }
  }

  val live: ZLayer[PrintService with StoreService, Nothing, PrinterStoreService] =
    ZLayer
    .fromServices[ConsolePrinter.Service, ConsoleStore.Service, ConsolePrinterStore.Service]( 
      (printer, store) => new Service(printer, store)
    )

  def printAndStore(message: Message): ZIO[PrinterStoreService, Throwable, Message] = ZIO.accessM(_.get.printAndStore(message))
}