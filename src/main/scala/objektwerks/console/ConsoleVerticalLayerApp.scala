package objektwerks.console

import objektwerks.Message
import zio.{App, ExitCode, ZEnv, ZIO}

object ConsoleVerticalLayerApp extends App {
  import ConsoleLayers._

  override def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] =
    ConsolePrinterStore.printAndStore(Message("Vertical layer app message!"))
      .provideLayer(printerStoreVerticalLayer)
      .catchAll(error => ZIO.succeed(error.printStackTrace()).exitCode)
      .map { message =>
        println(s"[App] Printed and stored message: $message")
        ExitCode.success
      }
}
