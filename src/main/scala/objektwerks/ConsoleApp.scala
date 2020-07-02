package objektwerks

import java.io.IOException

import zio.console._
import zio.App
import zio.ZIO
import zio.URIO
import zio.ExitCode
import zio.ZEnv

object ConsoleApp extends App {
  val program: ZIO[Console, IOException, Unit] = for {
    _    <- putStrLn("Hello! What is your name?")
    name <- getStrLn
    _    <- putStrLn(s"Hello, $name, welcome to ZIO!")
  } yield ()

  def run(args: List[String]): URIO[ZEnv, ExitCode] = program.fold(_ => ExitCode.failure, _ => ExitCode.success)
}