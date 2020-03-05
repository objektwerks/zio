package objektwerks

import zio.test._
import zio.test.Assertion._

object ZioSpecTest extends DefaultRunnableSpec {
  def spec = suite("zio.spec.test")(
    test("test") {
      assert(1 + 2)(equalTo(3))
    }
  )
}