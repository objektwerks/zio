package zio

import org.scalatest.{FunSuite, Matchers}
import scalaz.zio.{DefaultRuntime, ZIO}

class ZioTest extends FunSuite with Matchers {
  test("effects") {
    val runtime = new DefaultRuntime {}

    runtime.unsafeRun( ZIO.succeed(33) ) shouldBe 33
    runtime.unsafeRun( ZIO.succeedLazy( List(1, 2, 3).sum ) ) shouldBe 6
  }
}