package objektwerks

import zio.{App, ExitCode, ZEnv, ZIO}

object ConsoleVerticalLayerApp extends App {
  import ConsoleLayers._
  import ConsolePrinterStore._

  override def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] =
    printAndStore( Message("Vertical layer app message!") )
      .provideLayer(printerStoreVerticalLayer)
      .catchAll(error => ZIO.succeed(error.printStackTrace()).exitCode )
      .map { message =>
        println(s"[App] Printed and stored message: $message")
        ExitCode.success
      }
}