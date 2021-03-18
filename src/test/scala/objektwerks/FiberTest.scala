package objektwerks

import zio.ZIO
import zio.test.Assertion.{equalTo, isTrue}
import zio.test._

object FiberTest extends ZioTest {
  import Files._

  def spec: Spec[Environment, TestFailure[Nothing], TestSuccess] = suite("fiber.test")(
    testM("fiber > hello world") {
      for {
        helloFiber <- ZIO.succeed("Hello, ").fork
        worldFiber <- ZIO.succeed("world!").fork
        fiber = helloFiber zip worldFiber
        tuple <- fiber.join
      } yield {
        val (hello, world) = tuple
        val helloWorld = hello + world
        assert( helloWorld )( equalTo("Hello, world!") )
      }
    },

    test("fiber > file") {
      val fileContent: ZIO[Any, Throwable, String] = for {
        fiber <- file("build.sbt").fork
        source <- fiber.join
      } yield source.mkString
      assert( runtime.unsafeRun(fileContent).nonEmpty )( isTrue )
    }
  )
}