package objektwerks

import zio.{ZEnv, ZLayer}

object ConsolePrinterStoreLayers {
  import ConsoleConfig._
  import ConsolePrinter._
  import ConsoleStore._
  import ConsolePrinterStore._

  /* 
    ++ alias and
    >>> alias to
  */
  val configEnvHorizontalLayer: ZLayer[Any, Nothing, Config with ZEnv] = ConsoleConfig.live ++ ZEnv.live
  val printerStoreHorizontalLayer: ZLayer[Any, Nothing, Printer with Store] = ConsolePrinter.live ++ ConsoleStore.live
  val printerStoreVerticalLayer: ZLayer[Any, Throwable, PrinterStore] = printerStoreHorizontalLayer >>> ConsolePrinterStore.live
}