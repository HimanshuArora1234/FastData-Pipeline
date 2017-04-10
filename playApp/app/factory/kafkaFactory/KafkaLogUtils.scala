package factory.kafkaFactory

import java.time.LocalDateTime
import java.util.UUID

import play.api.libs.json.{JsString, Json}
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
    * @return String
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
    * @return String
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
}