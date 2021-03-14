package objektwerks

import zio.{App, ExitCode, URIO, ZEnv, ZIO}
import zio.console._

object ConsoleApp extends App {
  import ConsoleConfig._

  val effect: ZIO[Console with Config, Throwable, Unit] = for {
    conf <- load("console.conf")
    _    <- putStrLn(conf.question)
    name <- getStrLn
    _    <- putStrLn(s"$name, ${conf.response}")
  } yield ()

  def run(args: List[String]): URIO[ZEnv, ExitCode] = 
    effect
      .provideCustomLayer(ConsoleConfig.live)
      .fold(_ => ExitCode.failure, _ => ExitCode.success)
}