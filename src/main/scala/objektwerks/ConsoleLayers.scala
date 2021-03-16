package objektwerks

import zio.ZLayer
import zio.logging._

object ConsoleLayers {
  import ConsolePrinter._
  import ConsoleStore._
  import ConsolePrinterStore._

  /* 
    ++ alias and
    >>> alias to
  */
  val loggingConfigHorizontalLayer = Logging.console() ++ ConsoleConfig.live
  val printerStoreHorizontalLayer: ZLayer[Any, Nothing, Printer with Store] = ConsolePrinter.live ++ ConsoleStore.live
  val printerStoreVerticalLayer: ZLayer[Any, Throwable, PrinterStore] = printerStoreHorizontalLayer >>> ConsolePrinterStore.live
}