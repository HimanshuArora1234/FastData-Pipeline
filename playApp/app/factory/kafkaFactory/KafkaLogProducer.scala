package factory.kafkaFactory

import java.util.Properties
import javax.inject.{Inject, Singleton}

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import play.api.Configuration

/**
  * Created by himanshu on 06/04/17.
  */
@Singleton
class KafkaLogProducer @Inject() (configuration: Configuration) {

  lazy private val props = new Properties
  props.put("bootstrap.servers", configuration.getString("kafka.bootstrap.servers").getOrElse(""))
  props.put("key.serializer", configuration.getString("kafka.serializer.class").getOrElse(""))
  props.put("value.serializer", configuration.getString("kafka.serializer.class").getOrElse(""))
  props.put("acks", configuration.getString("kafka.acks").getOrElse(""))
  props.put("reconnect.backoff.ms", configuration.getString("kafka.reconnect.backoff.ms").getOrElse(""))

  lazy private val topicName = configuration.getString("kafka.topicName").getOrElse("")

  lazy private val producer = new KafkaProducer[String, String](props)

  def send(msg: String) = {
    System.out.println(" //////////////////////////////// msg = " + msg + " Producer = " + producer)
    producer.send(new ProducerRecord[String, String](topicName, msg))
  }

  def close = producer.close()

}
