package objektwerks

import objektwerks.ConsoleConfig.Config
import zio.ZLayer
import zio.clock.Clock
import zio.console.Console
import zio.logging._

object ConsoleLayers {
  import ConsolePrinter._
  import ConsolePrinterStore._
  import ConsoleStore._

  /* 
    ++ alias and
    >>> alias to
  */
  val loggingConfigHorizontalLayer: ZLayer[Console with Clock with Any, Nothing, Logging with Config] =
    Logging.console() ++ ConsoleConfig.live
  val printerStoreHorizontalLayer: ZLayer[Any, Nothing, Printer with Store] =
    ConsolePrinter.live ++ ConsoleStore.live
  val printerStoreVerticalLayer: ZLayer[Any, Throwable, PrinterStore] =
    printerStoreHorizontalLayer >>> ConsolePrinterStore.live
}