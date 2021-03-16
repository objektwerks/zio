package objektwerks

import zio.test._
import zio.{Runtime, ZEnv}

trait ZioTest extends DefaultRunnableSpec {
  protected val runtime: Runtime[ZEnv] = Runtime.default
}