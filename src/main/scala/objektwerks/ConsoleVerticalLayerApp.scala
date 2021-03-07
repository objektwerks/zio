package objektwerks

import zio.{ExitCode, ZEnv, ZLayer, ZIO}

object ConsoleVerticalLayerApp extends zio.App {
  import ConsoleLayerService._
  import ConsoleLayerStore._
  import ConsoleLayerCompositeService._

  val serviceStoreLayer: ZLayer[Any, Nothing, ConsoleLayerServiceEnv with ConsoleLayerStoreEnv] =
    ConsoleLayerService.live ++ ConsoleLayerStore.live

  val compositeLayer: ZLayer[Any, Throwable, ConsoleLayerCompositeServiceEnv] =
    serviceStoreLayer >>> ConsoleLayerCompositeService.live

  override def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] =
    ConsoleLayerCompositeService.printAndStore( Message("Vertical layer test message!") )
      .provideLayer(compositeLayer)
      .catchAll(error => ZIO.succeed( error.printStackTrace() ).map(_ => ExitCode.failure) )
      .map { message =>
        println(s"[ConsoleVerticalLayerApp] Printed and stored message: $message")
        ExitCode.success
      }
}