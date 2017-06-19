package factory.kafkaFactory

import java.time.LocalDateTime
import java.util.UUID

import model.EventType
import model.EventType.EventType
import play.api.libs.json.{JsString, JsValue, Json}
import play.api.mvc.Request
import play.mvc.Http.RequestHeader
import play.api.mvc.Result


/**
  * Log utility object.
  * @author Himanshu
  */
object KafkaLogUtils {

  /**
    * Helper to create Json string message from request object.
    * @param request Request
    * @param uid Log unique id
    * @tparam A Body type
    * @return Json string
    */
  def toJson[A](request: Request[A], uid: String): String = {
    Json.obj(
      "uid" -> JsString(uid),
      "type" -> JsString("request"),
      "uri" -> JsString(request.uri),
      "method" -> JsString(request.method),
      "body" -> JsString(request.body.toString),
      "host" -> JsString(request.host),
      "remoteAddress" -> JsString(request.remoteAddress),
      "userAgent" -> JsString(request.headers.get("User-Agent").getOrElse("")),
      "when" -> JsString(LocalDateTime.now().toString)
    ).toString()
  }

  /**
    * Helper to create Json string message from result object.
    * @param result Result
    * @param time execution time in ms
    * @param uid Log unique id
    * @return Json string
    */
  def toJson(result: Result, time: Long, uid: String): String = {
    Json.obj(
      "uid" -> JsString(uid),
      "type" -> JsString("response"),
      "status" -> JsString(String.valueOf(result.asJava.status())),
      "executionTime-ms" -> JsString(String.valueOf(time)),
      "when" -> JsString(LocalDateTime.now().toString)
    ).toString()
  }

  /**
    * Helper to create Json string message for events.
    * @param eventType Event type
    * @param data data involved in the event
    * @return Json string
    */
  def toEventJson(eventType: EventType.Value, data: JsValue): String = {
    Json.obj(
      "event" -> eventType.toString,
      "data" -> data
    ).toString()
  }
}