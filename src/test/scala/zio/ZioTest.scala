package zio

import org.scalatest.{FunSuite, Matchers}
import scalaz.zio.ZIO

class ZioTest extends FunSuite with Matchers {
  test("effect success") {
    val success = ZIO.succeed(33)
    success.run
  }

  test("effect fail") {
    val fail = ZIO.fail(new IllegalArgumentException("illegal arg"))
    fail.run
  }
}