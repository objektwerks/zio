package objektwerks

import zio.Queue

class QueueTest extends ZioTest {
  test("queue") {
    val queueInt = for {
      queue  <- Queue.bounded[Int](3)
      _      <- queue.offer(3)
      result <- queue.take
    } yield result
    runtime.unsafeRun( queueInt ) shouldBe 3
  }
}