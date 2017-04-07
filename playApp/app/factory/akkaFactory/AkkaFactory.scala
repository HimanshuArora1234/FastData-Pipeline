package factory.akkaFactory

import javax.inject.{Inject, Singleton}

import akka.actor.{ActorSystem, Props}
import factory.kafkaFactory.KafkaLogProducer

/**
  * Created by himanshu on 06/04/17.
  */
@Singleton
class AkkaFactory @Inject()(actorSystem: ActorSystem, kafkaLogProducer: KafkaLogProducer) {
  val kafkaProducerActorRef = actorSystem.actorOf(Props(new KafkaProducerActor(kafkaLogProducer)), "kafka-log-actor")
}
