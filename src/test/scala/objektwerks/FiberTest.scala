package objektwerks

import zio.ZIO
import zio.test.Assertion.{equalTo, isTrue}
import zio.test._

object FiberTest extends ZioTest {
  def spec: Spec[Environment, TestFailure[Nothing], TestSuccess] = suite("fiber.test")(
    test("fiber") {
      val fileContent = for {
        fiber <- file("build.sbt").fork
        source <- fiber.join
      } yield source.mkString
      assert(runtime.unsafeRun( fileContent ).nonEmpty)(isTrue)

      val helloWorld = for {
        helloFiber <- ZIO.succeed("Hello, ").fork
        worldFiber <- ZIO.succeed("world!").fork
        fiber = helloFiber zip worldFiber
        tuple <- fiber.join
      } yield {
        val (hello, world) = tuple
        hello + world
      }
      assert(runtime.unsafeRun( helloWorld ))(equalTo("Hello, world!"))
    }
  )
}