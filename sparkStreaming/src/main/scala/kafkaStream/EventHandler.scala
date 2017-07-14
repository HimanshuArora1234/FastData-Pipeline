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
    * Helper function to prepare the data tuple for update/insert events.
    *
    * @param event Event to handle
    * @return data tuple
    */
  def handleUpdate(event: String): Option[(String, String, String)] =
    extractData(event).map(json =>
      ((json \ "uuid").get.as[String], (json \ "name").get.as[String], (json \ "email").get.as[String]))

  /**
    * Helper function to prepare the data tuple for delete events.
    *
    * @param event Event to handle
    * @return data tuple
    */
  def handleDelete(event: String): Option[Tuple1[String]] =
    extractData(event).map(json => Tuple1((json \ "uuid").get.as[String]))


  /**
    * Helper function to parse the event and extract the data part.
    *
    * @param event Event to be parsed
    * @return Data string if a valid event
    */
  private def extractData(event: String): Option[JsValue] = {
    val jsonData = (Json.parse(event) \ "data").get
    Option(jsonData)
  }

}
