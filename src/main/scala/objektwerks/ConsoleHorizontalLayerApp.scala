package objektwerks

import zio.{ExitCode, URIO, ZEnv, ZLayer, ZIO}

object ConsoleHorizontalLayerApp extends zio.App {
  import ConsolePrinter._
  import ConsoleStore._

  val serviceStoreLayer: ZLayer[Any, Nothing, PrintService with StoreService] =
    ConsolePrinter.live ++ ConsoleStore.live

  val program: ZIO[PrintService with StoreService, Throwable, Message] = for {
    m  <- print( Message("Horizontal layer test message!") )
    mm <- store(m)
  } yield mm

  def run(args: List[String]): URIO[ZEnv, ExitCode] =
    program
      .provideLayer(serviceStoreLayer)
      .catchAll(error => ZIO.succeed( error.printStackTrace() ).map(_ => ExitCode.failure) )
      .map { message =>
        println(s"[ConsoleHorizontalLayerApp] Printed and stored message: $message")
        ExitCode.success
      }
}