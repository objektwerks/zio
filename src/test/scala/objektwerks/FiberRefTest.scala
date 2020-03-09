package objektwerks

import zio.FiberRef
import zio.test.Assertion.equalTo
import zio.test._

object FiberRefTest extends ZioTest {
  def spec: Spec[Environment, TestFailure[Nothing], TestSuccess] = suite("fiber.ref.test")(
    test("fiber.ref") {
      val fiberRefInt = for {
        fiberRef <- FiberRef.make[Int](0)
        _        <- fiberRef.set(3)
        value    <- fiberRef.get
      } yield value
      assert(runtime.unsafeRun( fiberRefInt ))(equalTo(3))
    }
  )
}