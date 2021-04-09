package objektwerks.http

import zhttp.http.Response.HttpResponse
import zhttp.http.URL.Location
import zhttp.http._
import zhttp.service._
import zio._

/**
 * Will wait for new zio-http release!
 */
object NowClient extends App {
  val env: ZLayer[Any, Nothing, ChannelFactory with EventLoopGroup] = ChannelFactory.nio ++ EventLoopGroup.nio(0)
  val path: Path = Root / "now"
  val location: Location = Location.Absolute(Scheme.HTTP, "localhost", 7979)
  val url = URL( path, location )
  val alternateUrl: URL = URL.fromString("http://localhost:7979/now").get
  println(s"URL: ${url.toString}")
  println(s"Alternate URL: ${alternateUrl.toString}")
  /*
    URL: URL(/now,Absolute(HTTP,localhost,7979))
    Alternate URL: URL(/now,Absolute(HTTP,localhost,7979))
    The requested URI "/http:/localhost:7979/now" was not found on this server
   */
  val endpoint: Endpoint = ( Method.GET, alternateUrl )

  val effect = for {
    response <- Client.request( Request( endpoint ) )
    _        <- console.putStrLn {
                  response.asInstanceOf[HttpResponse].content match {
                    case HttpContent.Complete(data) => data
                    case _     => "No response received!"
                  }
                }
  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    effect.provideCustomLayer(env).exitCode
}