package objektwerks

import zio.ZIO
import zio.test.Assertion.{equalTo, isTrue}
import zio.test._

import scala.concurrent.Future
import scala.util.Try

object EffectTest extends ZioTest {
  def spec: Spec[Environment, TestFailure[Nothing], TestSuccess] = suite("effect.test")(
    test("effect") {
      assert(runtime.unsafeRun( ZIO.fail("fail").mapError(error => new Exception(error)).either ).isLeft)(isTrue)
      assert(runtime.unsafeRun( ZIO.succeed(33) ))(equalTo(33))
      assert(runtime.unsafeRun( ZIO.effectTotal( List(1, 2, 3).sum )))(equalTo(6))

      assert(runtime.unsafeRun( ZIO.fromOption(Some(3)).map(_ * 2) ))(equalTo(6))
      assert(runtime.unsafeRun( ZIO.fromEither(Right(3)).map(_ * 2) ))(equalTo(6))
      assert(runtime.unsafeRun( ZIO.fromTry(Try(18 / 3)) ))(equalTo(6))
      assert(runtime.unsafeRun( ZIO.fromFunction((i: Int) => i * i).provide(3) ))(equalTo(9))
      assert(runtime.unsafeRun( ZIO.fromFuture { implicit ec => Future(3).map(_ * 3) } ))(equalTo(9))
    }
  )
}