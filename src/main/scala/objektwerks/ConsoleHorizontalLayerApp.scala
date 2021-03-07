package objektwerks

import zio.{ExitCode, URIO, ZEnv, ZLayer, ZIO}

object ConsoleHorizontalLayerApp extends zio.App {
  import ConsoleLayerService._
  import ConsoleLayerStore._

  val serviceStoreLayer: ZLayer[Any, Nothing, ConsoleLayerServiceEnv with ConsoleLayerStoreEnv] =
    ConsoleLayerService.live ++ ConsoleLayerStore.live

  val program: ZIO[ConsoleLayerServiceEnv with ConsoleLayerStoreEnv, Throwable, Unit] = for {
    message <- print( Message("Horizontal layer test message!") )
    _       <- store(message)
  } yield()

  def run(args: List[String]): URIO[ZEnv, ExitCode] = 
    program
      .provideLayer(serviceStoreLayer)
      .fold(_ => ExitCode.failure, _ => ExitCode.success)
}