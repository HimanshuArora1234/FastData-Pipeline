package kafkaStream

import java.util.UUID

import play.api.libs.json.{JsObject, JsValue, Json}

/**
  * Object to handle the event-sourcing events.
  *
  * @author Himanshu
  */
object EventHandler {

  /**
    * Helper function generate cassandra query according to event type.
    * @param event Event to handle
    * @return Query string if a valid event
    */
  def handle(event: String): Option[String] =  event match {
    case eventStr if eventStr.contains("ProfileAdded") => extractData(event, "ProfileAdded").map(json =>
      s"INSERT INTO userDB.profile (uuid, name, email) VALUES(${(json \ "uuid").get.toString}, " +
        s"${(json \ "name").get.toString}, ${(json \ "email").get.toString});"
    )
    case eventStr if eventStr.contains("ProfileUpdated") => extractData(event, "ProfileUpdated").map(json =>
      s"UPDATE userDB.profile name=${(json \ "name").get.toString}, email=${(json \ "email").get.toString} " +
        s"WHERE uuid=${(json \ "uuid").get.toString};"

    )
    case eventStr if eventStr.contains("ProfileDeleted") => extractData(event, "ProfileDeleted").map(json =>
      s"DELETE FROM  userDB.profile WHERE uuid=${(json \ "uuid").get.toString};"
    )
    case _ => None
  }

  /**
    * Helper function to parse the event and extract the data part.
    * @param event Event to be parsed
    * @return Data string if a valid event
    */
  private def extractData(event: String, eventType: String): Option[JsValue] = {
    val jsonData = (Json.parse(event) \ "data").get
    if (eventType == "ProfileAdded") {
      Option(jsonData.as[JsObject].deepMerge(Json.obj("uuid" -> UUID.randomUUID().toString)))
    } else {
      Option(jsonData)
    }
  }

}
