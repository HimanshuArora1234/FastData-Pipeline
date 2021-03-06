package factory.kafkaFactory

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

/**
  * Object to create Kafka producer client.
  * @author Himanshu
  */
object KafkaLogProducer {

  private val configuration = play.Configuration.root()

  private val props = new Properties
  props.put("bootstrap.servers", configuration.getString("kafka.bootstrap.servers"))
  props.put("key.serializer", configuration.getString("kafka.serializer.class"))
  props.put("value.serializer", configuration.getString("kafka.serializer.class"))
  props.put("acks", configuration.getString("kafka.acks"))
  props.put("reconnect.backoff.ms", configuration.getString("kafka.reconnect.backoff.ms"))

  private val topicName = configuration.getString("kafka.topicName")

  private val producer = new KafkaProducer[String, String](props)

  // Function to send messages to kafka topic
  def send(msg: String) = producer.send(new ProducerRecord[String, String](topicName, msg))

  def close = producer.close()

}
