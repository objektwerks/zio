package objektwerks

sealed trait Entity extends Product with Serializable
case class Message(text: String) extends Entity

sealed trait Command extends Entity
final case class Ping() extends Command

sealed trait Event extends Entity
final case class Pinged(datetime: String) extends Event