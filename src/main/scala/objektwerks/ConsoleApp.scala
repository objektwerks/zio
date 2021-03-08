package objektwerks

import java.io.IOException

import zio.console._
import zio.{App, ExitCode, URIO, ZEnv, ZIO}

object ConsoleApp extends App {
  val effect: ZIO[Console, IOException, Unit] = for {
    _    <- putStrLn("Hello! What is your name?")
    name <- getStrLn
    _    <- putStrLn(s"Hello, $name, welcome to ZIO!")
  } yield ()

  def run(args: List[String]): URIO[ZEnv, ExitCode] = effect.fold(_ => ExitCode.failure, _ => ExitCode.success)
}