package objektwerks

import java.io.IOException

import zio.console._
import zio.{App, ExitCode, URIO, ZEnv, ZIO}

object ConsoleApp extends App {
  val effect: ZIO[ZEnv, IOException, Unit] = for {
    _    <- putStrLn("What is your name?")
    name <- getStrLn
    _    <- putStrLn(s"$name, welcome to ZIO!")
  } yield ()

  def run(args: List[String]): URIO[ZEnv, ExitCode] = effect.fold(_ => ExitCode.failure, _ => ExitCode.success)
}