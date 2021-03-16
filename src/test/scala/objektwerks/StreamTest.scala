package objektwerks

import zio.stream._
import zio.test.Assertion._
import zio.test._

object StreamTest extends ZioTest {
  def spec: Spec[Environment, TestFailure[Nothing], TestSuccess] = suite("stream.test")(
    test("stream") {
      assert( runtime.unsafeRun( Stream(1, 2, 3).run(Sink.foldLeft[Int, Int](0)(_ + _)) ))(equalTo(6))
    }
  )
}