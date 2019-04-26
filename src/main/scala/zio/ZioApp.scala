package zio

import scalaz.zio.console.{Console, getStrLn, putStrLn}
import scalaz.zio.{App, ZIO}

object ZioApp extends App {
  val error = 1
  val success = 0

  def run(args: List[String]): ZIO[Console, Nothing, Int] = program.fold(_ => error, _ => success)

  val program = for {
    _ <- putStrLn("Hello! What is your name?")
    n <- getStrLn
    _ <- putStrLn(s"Hello, $n, welcome to ZIO!")
  } yield ()
}