package objektwerks

import zio.stream._
import zio.test._
import zio.test.Assertion._

object StreamTest extends ZioTest {
  def spec: Spec[Environment, TestFailure[Nothing], TestSuccess] = suite("stream.test")(
    test("stream") {
      assert( runtime.unsafeRun( Stream(1, 2, 3).fold(0)(_ + _) ) )(equalTo(6))
      assert( runtime.unsafeRun( Stream(1, 2, 3).map(_ * 2).fold(0)(_ + _)) )(equalTo(12))
      assert( runtime.unsafeRun( Stream(1, 2, 3).merge(Stream(4, 5, 6)).fold(0)(_ + _)) )(equalTo(21))

      assert( runtime.unsafeRun( Stream(1, 2, 3).run(Sink.await[Int]) ))(equalTo(1))
      assert( runtime.unsafeRun( Stream(1, 2, 3).run(Sink.identity[Int].optional) ))(equalTo(Option(1)))
      assert( runtime.unsafeRun( Stream(1, 2, 3).run(Sink.collectAll[Int]) ))(equalTo(List(1, 2, 3)))
      assert( runtime.unsafeRun( Stream(1, 2, 3).run(Sink.collectAllWhile[Int](_ < 3)) ))(equalTo(List(1, 2)))
      assert( runtime.unsafeRun( Stream(1, 2, 3).run(Sink.foldLeft[Int, Int](0)(_ + _)) ))(equalTo(6))
      assert( runtime.unsafeRun( Stream(1, 2, 3).run(Sink.fromFunction[Int, Int](_ * 2).collectAll) ))(equalTo(List(2, 4, 6)))
    }
  )
}