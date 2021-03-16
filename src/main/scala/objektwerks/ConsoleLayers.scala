package objektwerks

import zio.ZLayer

object ConsoleLayers {
  import ConsolePrinter._
  import ConsoleStore._
  import ConsolePrinterStore._

  /* 
    ++ alias and
    >>> alias to
  */
  val printerStoreHorizontalLayer: ZLayer[Any, Nothing, Printer with Store] = ConsolePrinter.live ++ ConsoleStore.live
  val printerStoreVerticalLayer: ZLayer[Any, Throwable, PrinterStore] = printerStoreHorizontalLayer >>> ConsolePrinterStore.live
}