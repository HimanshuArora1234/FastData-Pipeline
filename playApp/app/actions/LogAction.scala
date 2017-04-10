package actions

/**
  * Created by himanshu on 02/04/17.
  */

import java.util.UUID

import factory.akkaFactory.{AkkaFactory, LogMessage}
import factory.kafkaFactory.KafkaLogUtils
import play.api.mvc._

import scala.concurrent.Future

/**
  * ActionBuilder to send the request logs to kafka.
  * @author Himanshu
  */
object LogAction extends ActionBuilder[Request] {
  def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    val uid = UUID.randomUUID().toString
    AkkaFactory.kafkaProducerActorRef ! new LogMessage(KafkaLogUtils.toJson(request, uid))
    block(Request.apply(request.copy(headers = request.headers.add(("uid", uid))), request.body))
  }

  /**
    * Helper function to add request log uid to the response header.
    * @param result Result
    * @param request Request
    * @tparam A Type of request body
    * @return Result
    */
  def toResponse[A](result: Result)(implicit request: Request[A]): Result =
    request.headers.get("uid").map(uid => result.withHeaders(("uid", uid))).getOrElse(result)
}