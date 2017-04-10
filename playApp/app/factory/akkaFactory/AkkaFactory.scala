package factory.akkaFactory

import javax.inject.{Inject, Singleton}

import akka.actor.{ActorSystem, Props}
import factory.kafkaFactory.KafkaLogProducer

/**
  * Object to create the akka actors.
  * @author Himanshu
  */
object AkkaFactory {
  val kafkaProducerActorRef = ActorSystem().actorOf(Props(new KafkaProducerActor()), "kafka-log-actor")
}
