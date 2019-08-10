package objektwerks

import zio.ZIO

class FiberTest extends ZioTest {
  test("fiber") {
    val fileContent = for {
      fiber <- file("build.sbt").fork
      source <- fiber.join
    } yield source.mkString
    runtime.unsafeRun( fileContent ).nonEmpty shouldBe true

    val helloWorld = for {
      helloFiber <- ZIO.succeed("Hello, ").fork
      worldFiber <- ZIO.succeed("world!").fork
      fiber = helloFiber zip worldFiber
      tuple <- fiber.join
    } yield {
      val (hello, world) = tuple
      hello + world
    }
    runtime.unsafeRun( helloWorld ) shouldBe "Hello, world!"
  }
}