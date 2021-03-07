package objektwerks

import zio.{ExitCode, ZEnv, ZIO}

object ConsoleLayerApp extends zio.App {
  override def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] =
    ConsoleLayerService
      .notify( Message("Test message!"))
      .provideLayer(ConsoleLayerService.live)
      .exitCode
}