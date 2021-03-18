package objektwerks

import zio.Queue
import zio.test.Assertion.equalTo
import zio.test._

object QueueTest extends ZioTest {
  def spec: Spec[Environment, TestFailure[Nothing], TestSuccess] = suite("queue.test")(
    testM("queue") {
      for {
        queue  <- Queue.bounded[Int](3)
        _      <- queue.offer(3)
        result <- queue.take
      } yield assert( result )( equalTo(3) )
    }
  )
}