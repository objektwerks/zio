package objektwerks

import zio.stream._
import zio.test.Assertion._
import zio.test._

object StreamTest extends ZioTest {
  def spec: Spec[Environment, TestFailure[Nothing], TestSuccess] = suite("stream.test")(
    testM("fold left") {
      assertM( Stream(1, 2, 3).run( Sink.foldLeft[Int, Int](0)(_ + _) ) )( equalTo(6) )
    },

    testM("fold") {
      assertM( Stream(1, 2, 3).fold(0)(_ + _) )( equalTo(6) )
    },

    testM("map > fold") {
      assertM( Stream(1, 2, 3).map(_ * 2).fold(0)(_ + _) )( equalTo(12) )
    },

    testM("merge > fold") {
      assertM( Stream(1, 2, 3).merge(Stream(4, 5, 6)).fold(0)(_ + _) )( equalTo(21) )
    }
  )
}