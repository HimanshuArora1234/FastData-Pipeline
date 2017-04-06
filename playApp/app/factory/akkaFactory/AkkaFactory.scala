package factory.akkaFactory

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem

/**
  * Created by himanshu on 06/04/17.
  */
@Singleton
class AkkaFactory @Inject()(actorSystem: ActorSystem) {
  val kafkaProducerActorRef = actorSystem.actorOf(KafkaProducerActor.props, "kafka-log-actor")
}
