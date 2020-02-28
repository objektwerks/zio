package objektwerks

import zio.{App, ZIO}
import zio.console._

object ConsoleApp extends App {
  val error = 1
  val success = 0

  def run(args: List[String]): ZIO[Console, Nothing, Int] = program.fold(_ => error, _ => success)

  val program = for {
    _    <- putStrLn("Hello! What is your name?")
    name <- getStrLn
    _    <- putStrLn(s"Hello, $name, welcome to ZIO!")
  } yield ()
}