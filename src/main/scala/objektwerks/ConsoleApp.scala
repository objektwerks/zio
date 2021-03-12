package objektwerks

import java.io.IOException

import zio.console._
import zio.{App, ExitCode, URIO, ZEnv, ZIO}

object ConsoleApp extends App {
  // How to mixin a zio-config layer in lieu of the below code?
  val conf = ConsoleConfig.toConfig("console.conf")

  val effect: ZIO[ZEnv, IOException, Unit] = for {
    _    <- putStrLn(conf.question)
    name <- getStrLn
    _    <- putStrLn(s"$name, ${conf.response}")
  } yield ()

  def run(args: List[String]): URIO[ZEnv, ExitCode] = 
    effect
      .fold(_ => ExitCode.failure, _ => ExitCode.success)
}