package model

import play.api.libs.json.Format

/**
  * Event types.
  */
object EventType extends Enumeration {

  import model.enumFormat

  type EventType = Value

  val ProfileAdded = Value("ProfileAdded")
  val ProfileUpdated = Value("ProfileUpdated")
  val ProfileDeleted = Value("ProfileDeleted")

  implicit val eventFmt: Format[EventType.Value] = enumFormat(EventType)
}
