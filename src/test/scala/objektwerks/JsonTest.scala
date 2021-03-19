package objektwerks

import zio.json._
import zio.test.Assertion.equalTo
import zio.test.{Spec, TestFailure, TestSuccess, assert}

sealed trait Entity extends Product with Serializable
final case class Person(name: String, age: Int) extends Entity
object Person {
  implicit val decoder: JsonDecoder[Person] = DeriveJsonDecoder.gen[Person]
  implicit val encoder: JsonEncoder[Person] = DeriveJsonEncoder.gen[Person]
}

object JsonTest extends ZioTest {
  import Person._

  def spec: Spec[Environment, TestFailure[Nothing], TestSuccess] = suite("json.test")(
    test("json") {
      val person = Person("Fred Flintstone", 39)
      val json = person.toJson
      val clone = json.fromJson.getOrElse( Person("", -1))
      assert( clone )( equalTo( person ) )
    }
  )
}