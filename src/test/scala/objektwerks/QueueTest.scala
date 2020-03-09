package objektwerks

import zio.Queue
import zio.test.Assertion.equalTo
import zio.test._

object QueueTest extends ZioTest {
  def spec: Spec[Environment, TestFailure[Nothing], TestSuccess] = suite("queue.test")(
    test("queue") {
      val queueInt = for {
        queue  <- Queue.bounded[Int](3)
        _      <- queue.offer(3)
        result <- queue.take
      } yield result
      assert(runtime.unsafeRun( queueInt ))(equalTo(3))
    }
  )
}