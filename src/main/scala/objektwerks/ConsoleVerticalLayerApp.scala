package objektwerks

import zio.{ExitCode, ZEnv, ZLayer, ZIO}

object ConsoleVerticalLayerApp extends zio.App {
  import ConsolePrinter._
  import ConsoleStore._
  import ConsolePrinterStore._

  val printerStoreHorizontalLayer: ZLayer[Any, Nothing, Printer with Store] = ConsolePrinter.live ++ ConsoleStore.live
  val printerStoreVerticalLayer: ZLayer[Any, Throwable, PrinterStore] = printerStoreHorizontalLayer >>> ConsolePrinterStore.live

  override def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] =
    ConsolePrinterStore.printAndStore( Message("Vertical layer app message!") )
      .provideLayer(printerStoreVerticalLayer)
      .catchAll(error => ZIO.succeed( error.printStackTrace() ).map(_ => ExitCode.failure) )
      .map { message =>
        println(s"[App] Printed and stored message: $message")
        ExitCode.success
      }
}