package objektwerks.http

import zhttp.http._
import zhttp.service._
import zio._

object NowClient extends App {
  val env: ZLayer[Any, Nothing, ChannelFactory with EventLoopGroup] = ChannelFactory.auto ++ EventLoopGroup.auto()

  val effect = for {
    response <- Client.request("http://localhost:7979/now")
    _        <- console.putStrLn {
                  response.content match {
                    case HttpContent.Complete(data) => data
                    case _     => "No response received!"
                  }
                }
  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    effect
      .provideCustomLayer(env)
      .exitCode
}