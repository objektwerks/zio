package objektwerks

sealed trait Entity extends Product with Serializable
case class Message(text: String) extends Entity