package objektwerks.http

import zhttp.http.Response.HttpResponse
import zhttp.http._
import zhttp.service._
import zio._

object NowClient extends App {
  val env: ZLayer[Any, Nothing, ChannelFactory with EventLoopGroup] = ChannelFactory.nio ++ EventLoopGroup.nio(0)
  val url: URL = URL( Root / "http://localhost:7979" / "now" )
  println(s"URL: ${url.toString}")
  val endpoint: Endpoint = ( Method.GET, url )

  val effect = for {
    response <- Client.request( Request( endpoint ) )
    _        <- console.putStrLn {
                  response.asInstanceOf[HttpResponse].content match {
                    case HttpContent.Complete(data) => data
                    case _     => "No response received!"
                  }
                }
  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = effect.provideCustomLayer(env).exitCode
}