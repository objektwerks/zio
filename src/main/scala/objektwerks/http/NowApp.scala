package objektwerks.http

import java.time.Instant

import zio._
import zhttp.http._
import zhttp.service.Server

object NowApp extends App {
  val router = Http.collect[Request] {
    case Method.GET -> Root / "now" => Response.text( Instant.now.toString )
  }

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = Server.start(7979, router).exitCode
}