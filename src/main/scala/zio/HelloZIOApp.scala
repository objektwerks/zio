package zio

import scalaz.zio.console.{Console, getStrLn, putStrLn}
import scalaz.zio.{App, ZIO}

object HelloZIOApp extends App {
  def run(args: List[String]): ZIO[Console, Nothing, Int] = program.fold(_ => 1, _ => 0)

  val program = for {
    _ <- putStrLn("Hello! What is your name?")
    n <- getStrLn
    _ <- putStrLn(s"Hello, $n, welcome to ZIO!")
  } yield ()
}