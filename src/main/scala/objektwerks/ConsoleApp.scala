package objektwerks

import java.io.IOException

import zio.console._
import zio.{App, ZIO}

object ConsoleApp extends App {
  val program: ZIO[Console, IOException, Unit] = for {
    _    <- putStrLn("Hello! What is your name?")
    name <- getStrLn
    _    <- putStrLn(s"Hello, $name, welcome to ZIO!")
  } yield ()

  val error = 1
  val success = 0

  def run(args: List[String]): ZIO[Console, Nothing, Int] = program.fold(_ => error, _ => success)
}