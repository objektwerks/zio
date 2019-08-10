package objektwerks

import zio.FiberRef

class FiberRefTest extends ZioTest {
  test("fiber ref") {
    val fiberRefInt = for {
      fiberRef <- FiberRef.make[Int](0)
      _        <- fiberRef.set(3)
      value    <- fiberRef.get
    } yield value
    runtime.unsafeRun( fiberRefInt ) shouldBe 3
  }
}