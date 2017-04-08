package factory.akkaFactory

import javax.inject.Inject

import akka.actor.{Actor, Props}
import factory.kafkaFactory.KafkaLogProducer

//Protocol for KafkaProducerActor
case class LogMessage(msg: String)

/**
  * Created by himanshu on 06/04/17.
  */
class KafkaProducerActor extends Actor {

  override def receive = {
    case LogMessage(msg) => KafkaLogProducer.send(msg)
  }

  override def postStop(): Unit = {
    super.postStop()
    KafkaLogProducer.close
  }
}

