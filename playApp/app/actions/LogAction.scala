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
    block(request.copy(headers = request.headers.add(("uid", uid.toString))))
  }
}