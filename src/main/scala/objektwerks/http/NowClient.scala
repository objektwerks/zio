package objektwerks.http

import zhttp.service._

import zio._

object NowClient extends App {
  val env: ZLayer[Any, Nothing, ChannelFactory with EventLoopGroup] = ChannelFactory.auto ++ EventLoopGroup.auto()

  val effect = for {
    response <- Client.request("http://localhost:7979/now")
    now      <- response.getBodyAsString
    _        <- console.putStrLn(now)
  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = effect.provideCustomLayer(env).exitCode
}