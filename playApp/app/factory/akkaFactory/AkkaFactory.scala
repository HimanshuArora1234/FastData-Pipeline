package factory.akkaFactory

import javax.inject.{Inject, Singleton}

import akka.actor.{ActorSystem, Props}
import factory.kafkaFactory.KafkaLogProducer

/**
  * Created by himanshu on 06/04/17.
  */
object AkkaFactory {
  val kafkaProducerActorRef = ActorSystem().actorOf(Props(new KafkaProducerActor()), "kafka-log-actor")
}
