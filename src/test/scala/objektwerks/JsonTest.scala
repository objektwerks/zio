package objektwerks

import zio.json._
import zio.test.Assertion.{equalTo, isTrue}
import zio.test.{Spec, TestFailure, TestSuccess, assert}

object JsonTest extends ZioTest {
  import Person._

  def spec: Spec[Environment, TestFailure[Nothing], TestSuccess] = suite("json.test")(
    test("json") {
      val person = Person("Fred Flintstone", 25)
      val json = person.toJson
      val other = json.fromJson.getOrElse( Person("", -1) )
      assert( other )( equalTo( person ) ) &&
      assert( other.isInstanceOf[Entity])( isTrue )
    }
  )
}