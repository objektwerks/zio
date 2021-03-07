package objektwerks

import zio.{ExitCode, URIO, ZEnv, ZLayer, ZIO}

object ConsoleHorizontalLayerApp extends zio.App {
  import ConsolePrinter._
  import ConsoleStore._

  val printerStoreHorizontalLayer: ZLayer[Any, Nothing, Printer with Store] = ConsolePrinter.live ++ ConsoleStore.live

  val program: ZIO[Printer with Store, Throwable, Message] = for {
    m  <- print( Message("Horizontal layer app message!") )
    mm <- store(m)
  } yield mm

  def run(args: List[String]): URIO[ZEnv, ExitCode] =
    program
      .provideLayer(printerStoreHorizontalLayer)
      .catchAll(error => ZIO.succeed( error.printStackTrace() ).map(_ => ExitCode.failure) )
      .map { message =>
        println(s"[App] Printed and stored message: $message")
        ExitCode.success
      }
}