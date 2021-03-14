package objektwerks

import java.io.IOException

import zio.console._
import zio.{App, ExitCode, URIO, ZIO}

object ConsoleApp extends App {
  // How to mixin a zio-config layer in lieu of the below code?
  // See ConsoleConfig and ConsolePrinterStoreLayers
  val conf = ConsoleConfig.toConfig("console.conf")

  val effect: ZIO[Console, IOException, Unit] = for {
    _    <- putStrLn(conf.question)
    name <- getStrLn
    _    <- putStrLn(s"$name, ${conf.response}")
  } yield ()

  def run(args: List[String]): URIO[Console, ExitCode] = effect.fold(_ => ExitCode.failure, _ => ExitCode.success)
}