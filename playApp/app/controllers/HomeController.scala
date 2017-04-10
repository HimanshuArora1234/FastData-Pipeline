package controllers

import javax.inject._

import actions.LogAction
import akka.actor.ActorSystem
import factory.akkaFactory.{AkkaFactory, LogMessage}
import factory.kafkaFactory.KafkaLogProducer
import play.api._
import play.api.mvc._
import LogAction._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
  * @author Himanshu
 */
@Singleton
class HomeController @Inject() () extends Controller {

  def index = LogAction { implicit request =>
    toResponse(Ok("ok"))
  }

}
