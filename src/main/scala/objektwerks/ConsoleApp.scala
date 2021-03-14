package objektwerks

import zio.{App, ExitCode, URIO, ZEnv, ZIO}
import zio.console._
import zio.logging._

object ConsoleApp extends App {
  import ConsoleConfig._

  val env = Logging.console() ++ ConsoleConfig.live

  val effect: ZIO[Console with Logging with Config, Throwable, Unit] = for {
    _    <- log.info("Loading conf...")
    conf <- load("console.conf")
    _    <- log.info("Loaded conf...")
    _    <- putStrLn(conf.question)
    name <- getStrLn
    _    <- putStrLn(s"$name, ${conf.response}")
  } yield ()

  def run(args: List[String]): URIO[ZEnv, ExitCode] = 
    effect
      .provideCustomLayer(env)
      .fold(_ => ExitCode.failure, _ => ExitCode.success)
}