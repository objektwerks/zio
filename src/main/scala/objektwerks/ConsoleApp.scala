package objektwerks

import java.io.IOException

import zio.console._
import zio.{App, ExitCode, URIO, ZEnv, ZIO}

object ConsoleApp extends App {
  val conf = ConsoleConfig.load("./src/main/resources/console.conf").getOrElse(ConsoleConfig.empty)

  val effect: ZIO[ZEnv, IOException, Unit] = for {
    _    <- putStrLn(conf.question)
    name <- getStrLn
    _    <- putStrLn(s"$name, ${conf.response}")
  } yield ()

  def run(args: List[String]): URIO[ZEnv, ExitCode] = effect.fold(_ => ExitCode.failure, _ => ExitCode.success)
}