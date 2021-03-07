package objektwerks

import zio.{ExitCode, ZEnv, ZLayer, ZIO}

object ConsoleVerticalLayerApp extends zio.App {
  import ConsolePrinter._
  import ConsoleStore._
  import ConsolePrinterStoreService._

  val serviceStoreLayer: ZLayer[Any, Nothing, PrintService with StoreService] =
    ConsolePrinter.live ++ ConsoleStore.live

  val compositeLayer: ZLayer[Any, Throwable, PrinterStoreService] =
    serviceStoreLayer >>> ConsolePrinterStoreService.live

  override def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] =
    ConsolePrinterStoreService.printAndStore( Message("Vertical layer test message!") )
      .provideLayer(compositeLayer)
      .catchAll(error => ZIO.succeed( error.printStackTrace() ).map(_ => ExitCode.failure) )
      .map { message =>
        println(s"[ConsoleVerticalLayerApp] Printed and stored message: $message")
        ExitCode.success
      }
}