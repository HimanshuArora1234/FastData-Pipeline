package factory.kafkaFactory

import java.time.LocalDateTime
import java.util.UUID

import play.api.libs.json.{JsString, Json}
import play.api.mvc.Request
import play.mvc.Http.RequestHeader
import play.api.mvc.Result


/**
  * Created by himanshu on 08/04/17.
  */
object KafkaLogUtils {

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

  def toJson(result: Result, uid: String): String = {
    Json.obj(
      "uid" -> JsString(uid),
      "type" -> JsString("response"),
      "status" -> JsString(String.valueOf(result.asJava.status())),
      "when" -> JsString(LocalDateTime.now().toString)
    ).toString()
  }
}