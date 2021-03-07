package objektwerks

import zio.{ExitCode, ZEnv, ZLayer, ZIO}

object ConsoleHorizontalLayerApp extends zio.App {
  import ConsoleLayerService._
  import ConsoleLayerStore._

  val serviceStoreLayer: ZLayer[Any, Nothing, ConsoleLayerServiceEnv with ConsoleLayerStoreEnv] =
    ConsoleLayerService.live ++ ConsoleLayerStore.live

  override def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] =
    ConsoleLayerService
      .print( Message("Horizontal layer test message!") )
      .provideLayer(serviceStoreLayer) // ConsoleLayerStore is never called!
      .exitCode
}