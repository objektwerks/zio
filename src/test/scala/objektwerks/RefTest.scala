package objektwerks

import zio.Ref

class RefTest extends ZioTest {
  test("ref") {
    val refInt = for {
      ref     <- Ref.make(3)
      initial <- ref.get
      _       <- ref.set(initial * 3)
      result  <- ref.get
    } yield result
    runtime.unsafeRun( refInt ) shouldBe 9
  }
}