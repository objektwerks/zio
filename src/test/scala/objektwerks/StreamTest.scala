package objektwerks

import zio.stream._
import zio.test._
import zio.test.Assertion._

object StreamTest extends ZioTest {
  def spec: Spec[Environment, TestFailure[Nothing], TestSuccess] = suite("stream.test")(
    test("stream") {
      assert( runtime.unsafeRun( Stream(1, 2, 3).fold(0)(_ + _) ) )(equalTo(6))
    }
  )
}