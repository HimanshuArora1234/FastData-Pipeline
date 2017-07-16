package controllers

import java.util.UUID
import javax.inject._

import actions.LogAction
import actions.LogAction._
import factory.akkaFactory.{AkkaFactory, LogMessage}
import factory.cassandraFactory.CassandraHelper
import factory.kafkaFactory.KafkaLogUtils
import model.EventType
import play.api.libs.json.{JsObject, Json}
import play.api.mvc._
import scala.collection.convert.WrapAsScala.iterableAsScalaIterable

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  *
  * @author Himanshu
  */
@Singleton
class HomeController @Inject()() extends Controller {

  def index = LogAction { implicit request =>
    toUniqueResponse(Ok("ok"))
  }

  def addUserProfile = LogAction { implicit request =>
    request.body.asJson.map { json =>

      val uniqueJson = json.as[JsObject].deepMerge(Json.obj("uuid" -> UUID.randomUUID().toString))

      // Log ProfileAdded event to kafka event log topic (through AKKA actor)
      AkkaFactory.kafkaProducerActorRef ! new LogMessage(KafkaLogUtils.toEventJson(EventType.ProfileAdded, uniqueJson))

      // Eventual consistency applies, hence sending 202
      toUniqueResponse(Accepted)

    }.getOrElse {
      BadRequest("Expecting Json data")
    }

  }

  def updateUserProfile = LogAction { implicit request =>
    request.body.asJson.map { json =>

      // Log ProfileUpdated event to kafka event log topic (through AKKA actor)
      AkkaFactory.kafkaProducerActorRef ! new LogMessage(KafkaLogUtils.toEventJson(EventType.ProfileUpdated, json))

      // Eventual consistency applies, hence sending 202
      toUniqueResponse(Accepted)

    }.getOrElse {
      BadRequest("Expecting Json data")
    }

  }

  def deleteUserProfile(id: String) = LogAction { implicit request =>
    // Log ProfileDeleted event to kafka event log topic (through AKKA actor)
    AkkaFactory.kafkaProducerActorRef ! new LogMessage(KafkaLogUtils.toEventJson(EventType.ProfileDeleted, Json.obj("uuid" -> id)))

    // Eventual consistency applies, hence sending 202
    toUniqueResponse(Accepted)

  }

  def getAllProfiles = LogAction { implicit request =>
    toUniqueResponse(Ok(Json.toJson(CassandraHelper.getAll.toList.map(CassandraHelper.rowToJson(_)))))
  }

}
