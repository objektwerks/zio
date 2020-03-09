package objektwerks

import zio.Ref
import zio.test.Assertion.equalTo
import zio.test._

object RefTest extends ZioTest {
  def spec: Spec[Environment, TestFailure[Nothing], TestSuccess] = suite("ref.test")(
    test("ref") {
      val refInt = for {
        ref     <- Ref.make(3)
        initial <- ref.get
        _       <- ref.set(initial * 3)
        result  <- ref.get
      } yield result
      assert(runtime.unsafeRun( refInt ))(equalTo(9))
    }
  )
}