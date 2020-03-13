package objektwerks

import zio.Chunk
import zio.test.Assertion.equalTo
import zio.test._

object ChunkTest extends ZioTest {
  def spec: Spec[Environment, TestFailure[Nothing], TestSuccess] = suite("chunk.test")(
    test("chunk") {
      val chunk = Chunk(1, 2, 3)
      assert(chunk.collect(i => i * 2))(equalTo(Chunk(2, 4, 6)))
      assert(chunk.collect(i => i * 2).toList.sum)(equalTo(12))
    }
  )
}