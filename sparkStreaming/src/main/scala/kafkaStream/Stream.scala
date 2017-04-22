package kafkaStream

import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils

/**
  * Object to stream data from the kafka topic.
  *
  * @author Himanshu
  */
object Stream {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setAppName("kafka-streaming-app").setMaster("local") // Running on local machine

    // Create a StreamingContext with a 1 second batch size
    val ssc = new StreamingContext(sparkConf, Seconds(1))
    ssc.checkpoint("./spark-checkpoints") // Checkpointing meta-data to recover properly from faults

    val kafkaParams = Map[String, String]("metadata.broker.list" -> "172.17.0.3:9092") // Kafka broker
    val kafkaTopics = Set("log") // Kafak topic

    val kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc, kafkaParams, kafkaTopics)
    kafkaStream.checkpoint(Seconds(20))   // Checkpointing every 20 sec

    kafkaStream.foreachRDD(rdd => {
        println("----------------------- Messages received from kafka ----------------------------")
        rdd.foreach(record => println(record._2))
      }
    )

    ssc.start()
    ssc.awaitTermination()
  }

}
