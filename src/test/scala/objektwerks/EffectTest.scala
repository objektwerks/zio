package objektwerks

import zio.ZIO
import zio.test.Assertion.{equalTo, isLeft}
import zio.test._

import scala.concurrent.Future
import scala.util.Try

object EffectTest extends ZioTest {
  def spec: Spec[Environment, TestFailure[Nothing], TestSuccess] = suite("effect.test")(
    testM("fail") {
      assertM( ZIO.fail("fail").mapError(error => new Exception(error)).either )( isLeft )
    },

    testM("succeed") {
      assertM( ZIO.succeed(9) )( equalTo(9) )
    },

    testM("from function") {
      assertM( ZIO.fromFunction((i: Int) => i * i).provide(3) )( equalTo(9) )
    },

    testM("from either") {
      assertM( ZIO.fromEither(Right(3)).map(_ * 2) )( equalTo(6) )
    },

    testM("effect total") {
      assertM( ZIO.effectTotal( List(1, 2, 3).sum ))( equalTo(6) )
    },

    test("from option") { // Can't use with testM/assertM
      assert( runtime.unsafeRun( ZIO.fromOption(Some(3)).map(_ * 2) ))( equalTo(6) )
    },

    test("from try") { // Can't use with testM/assertM
      assert( runtime.unsafeRun( ZIO.fromTry(Try(18 / 3)) ))( equalTo(6) )
    },

    test("from future") { // Can't use with testM/assertM
      assert( runtime.unsafeRun( ZIO.fromFuture { implicit ec => Future(3).map(_ * 3) } ))( equalTo(9) )
    }
  )
}