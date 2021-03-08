package objektwerks

import zio.{App, ExitCode, URIO, ZEnv, ZIO}

object ConsoleHorizontalLayerApp extends App {
  import ConsolePrinter._
  import ConsoleStore._
  import ConsolePrinterStoreLayers._

  val effect: ZIO[Printer with Store, Throwable, Message] = for {
    printedMessage <- print( Message("Horizontal layer app message!") )
    storedMessage  <- store(printedMessage)
  } yield storedMessage

  def run(args: List[String]): URIO[ZEnv, ExitCode] =
    effect
      .provideLayer(printerStoreHorizontalLayer)
      .catchAll(error => ZIO.succeed( error.printStackTrace() ).map(_ => ExitCode.failure) )
      .map { message =>
        println(s"[App] Printed and stored message: $message")
        ExitCode.success
      }
}