package kafkaStream

import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.elasticsearch.spark._

/**
  * Object to stream data from the kafka topic.
  *
  * @author Himanshu
  */
object Stream {

  def main(args: Array[String]): Unit = {

    // Create spark context for this streaming job & run it on local machine as master
    val sparkConf = new SparkConf().setAppName("kafka-streaming-app").setMaster("local[4]")
    // Setting conf to write data to elastic search
    sparkConf.set("es.nodes", "localhost:9200")
    sparkConf.set("es.index.auto.create", "true")
    sparkConf.set("es.nodes.wan.only", "true")


    // Create a StreamingContext with a 1 second batch size
    val ssc = new StreamingContext(sparkConf, Seconds(1))
    // Checkpointing meta-data to recover properly from failures
    ssc.checkpoint("./spark-checkpoints")

    // Kafka broker to connect with
    val kafkaParams = Map[String, String]("metadata.broker.list" -> "127.0.0.1:9092")
    // Topic to stream from
    val kafkaTopics = Set("log")

    // Create a direct stream without receiver
    val kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc, kafkaParams, kafkaTopics)
    // Checkpointing spark meta-data every 60 sec
    kafkaStream.checkpoint(Seconds(60))

    // Actions applied to DStream
    kafkaStream.foreachRDD(rdd => {
        // Writing data to elastic search to fastdata index and log type
        rdd.map(record => record._2).saveJsonToEs("fastdata/log")

        // Printing data in console
        println(" <---- Message batch received from kafka ----> ")
        rdd.foreach(record => println(record._2))
      }
    )

    // Start streaming & wait indefinite for termination
    ssc.start()
    ssc.awaitTermination()
  }

}
