package objektwerks.http

import java.time.Instant

import zhttp.http._
import zhttp.service.Server
import zio._

object NowServer extends App {
  val router = Http.collect {
    case Method.GET -> Root / "now" => Response.text( Instant.now.toString )
  }

  override def run(args: List[String]): URIO[ZEnv, ExitCode] =
    Server.start(7979, router).exitCode
}