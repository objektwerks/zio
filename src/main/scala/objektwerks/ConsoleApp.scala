package objektwerks

import zio.console._
import zio.logging._
import zio.{App, ExitCode, URIO, ZEnv, ZIO}

object ConsoleApp extends App {
  import ConsoleConfig._
  import ConsoleLayers._

  val effect: ZIO[Console with Logging with Config, Throwable, Unit] = for {
    _    <- log.info("Loading console.conf...")
    conf <- load("console.conf")
    _    <- log.info("Loaded console.conf.")
    _    <- putStrLn(conf.question)
    name <- getStrLn
    _    <- putStrLn(s"$name, ${conf.response}")
  } yield ()

  def run(args: List[String]): URIO[ZEnv, ExitCode] = 
    effect
      .provideCustomLayer(loggingConfigHorizontalLayer)
      .exitCode
}