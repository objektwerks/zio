package objektwerks

import zio.test.Assertion.isTrue
import zio.test._
import zio.{Managed, Task, ZManaged}

import scala.io.BufferedSource

object ManagedTest extends ZioTest {
  import Resources._

  def spec: Spec[Environment, TestFailure[Nothing], TestSuccess] = suite("managed.test")(
    test("managed") {
      val managed: ZManaged[Any, Throwable, BufferedSource] = Managed.make(resource("build.sbt"))(close(_).ignore)
      val task: Task[String] = managed.use { source => stringify(source) }
      assert( runtime.unsafeRun( task ).nonEmpty )( isTrue )
    }
  )
}