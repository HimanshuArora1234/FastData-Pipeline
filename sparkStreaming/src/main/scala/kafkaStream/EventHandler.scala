package kafkaStream

import play.api.libs.json.Json

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
    case eventStr if eventStr.contains("ProfileAdded") => extractData(event)
    case eventStr if eventStr.contains("ProfileUpdated") => extractData(event)
    case eventStr if eventStr.contains("ProfileDeleted") => extractData(event)
    case _ => None
  }

  /**
    * Helper function to parse the event and extract the data part.
    * @param event Event to be parsed
    * @return Data string if a valid event
    */
  private def extractData(event: String): Option[String] = {
    Option((Json.parse(event) \ "data").get.toString)
  }

}
