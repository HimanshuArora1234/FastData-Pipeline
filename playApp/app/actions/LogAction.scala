package actions

/**
  * Created by himanshu on 02/04/17.
  */

import java.util.UUID

import factory.akkaFactory.{AkkaFactory, LogMessage}
import factory.kafkaFactory.KafkaLogUtils
import play.api.Logger
import play.api.mvc._

import scala.concurrent.Future

object LogAction extends ActionBuilder[Request] {
  def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    val uid = UUID.randomUUID().toString
    AkkaFactory.kafkaProducerActorRef ! new LogMessage(KafkaLogUtils.toJson(request, uid))
    block(Request.apply(request.copy(headers = request.headers.add(("uid", uid))), request.body))
  }

  def toResponse[A](result: Result)(implicit request: Request[A]): Result =
    result.withHeaders(("uid", request.headers.get("uid").get))
}