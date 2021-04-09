package objektwerks.http

import zhttp.http.URL.Location
import zhttp.http._
import zhttp.service._
import zio._

object NowClient extends App {
  val env: ZLayer[Any, Nothing, ChannelFactory with EventLoopGroup] = ChannelFactory.nio ++ EventLoopGroup.nio(0)
  val path: Path = Root / "now"
  val location: Location = Location.Absolute(Scheme.HTTP, "localhost", 7979)
  val url = URL( path, location )
  val endpoint: Endpoint = ( Method.GET, url )

  val effect = for {
    response <- Client.request( Request( endpoint ) )
    _        <- console.putStrLn {
                  response.content match {
                    case HttpContent.Complete(data) => data
                    case _     => "No response received!"
                  }
                }
  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    effect.provideCustomLayer(env).exitCode
}