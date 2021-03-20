package objektwerks.console

import objektwerks.Message
import zio.{Has, Task, ZIO, ZLayer}

object ConsolePrinterStore {
  import ConsolePrinter._
  import ConsoleStore._

  type PrinterStore = Has[ConsolePrinterStore.Service]

  class Service(printer: ConsolePrinter.Service, store: ConsoleStore.Service) {
    def printAndStore(message: Message): Task[Message] = {
      for {
        printedMessage <- printer.print(message)
        storedMessage <- store.store(printedMessage)
      } yield storedMessage
    }
  }

  val live: ZLayer[Printer with Store, Nothing, PrinterStore] =
    ZLayer.fromServices[ConsolePrinter.Service, ConsoleStore.Service, ConsolePrinterStore.Service](
      (printer, store) => new Service(printer, store)
    )

  def printAndStore(message: Message): ZIO[PrinterStore, Throwable, Message] = ZIO.accessM(_.get.printAndStore(message))
}
