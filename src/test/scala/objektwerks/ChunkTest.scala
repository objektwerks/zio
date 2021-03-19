package objektwerks

import zio.Chunk
import zio.test.Assertion.equalTo
import zio.test._

object ChunkTest extends ZioTest {
  private val chunk = Chunk(1, 2, 3)

  def spec: Spec[Environment, TestFailure[Nothing], TestSuccess] = suite("chunk.test")(
    test("++") {
      assert( Chunk(1, 2, 3) ++ Chunk(4, 5, 6) )( equalTo( Chunk(1, 2, 3, 4, 5, 6) ) )
    },

    test("collect") {
      assert( chunk.collect(i => i * 2) )( equalTo( Chunk(2, 4, 6) ) )
    },

    test("collect while") {
      assert(chunk.collectWhile { case i if i < 3 => i } )( equalTo( Chunk(1, 2) ) )
    },

    test("drop") {
      assert( chunk.drop(2) )( equalTo( Chunk(3) ) )
    },

    test("drop while") {
      assert( chunk.dropWhile(_ % 2 != 0) )( equalTo( Chunk(2, 3) ) )
    },

    test("take") {
      assert( chunk.take(2) )( equalTo( Chunk(1, 2) ) )
    },

    test("take while") {
      assert( chunk.takeWhile(_ % 2 != 0) )( equalTo( Chunk(1) ) )
    }
  )
}