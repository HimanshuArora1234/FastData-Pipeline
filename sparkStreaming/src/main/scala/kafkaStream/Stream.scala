package kafkaStream

import com.datastax.driver.core.Cluster
import com.datastax.spark.connector._
import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.elasticsearch.spark._

/**
  * Object to stream data from the kafka topic, process it accordingly and push it to the next steps of the
  * data pipeline.
  *
  * @author Himanshu
  */
object Stream {

  def main(args: Array[String]): Unit = {

    // Create spark context for this streaming job & run it on local machine (using 4 cores) as master
    val sparkConf = new SparkConf().setAppName("kafka-streaming-app").setMaster("local[4]")

    // Setting conf to write data to elastic search
    sparkConf.set("es.nodes", "localhost:9200")
    sparkConf.set("es.index.auto.create", "true")
    sparkConf.set("es.nodes.wan.only", "true")

    // Conf to connect with cassandra
    sparkConf.set("spark.cassandra.connection.host", "127.0.0.1")


    // Create a StreamingContext with a 1 second batch size
    val ssc = new StreamingContext(sparkConf, Seconds(1))

    // Set checkpoint directory to store meta-data to recover properly and faster from failures
    ssc.checkpoint("./spark-checkpoints")

    // Kafka broker(s) to connect with
    val kafkaParams = Map[String, String]("metadata.broker.list" -> "127.0.0.1:9092")

    // Topic to stream from
    val kafkaTopics = Set("log")

    // Create a direct stream without receiver to pull data from kafka brokers(s)
    // Checkpoint activation is must in case of direct streaming
    val kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc, kafkaParams, kafkaTopics)

    // Checkpointing spark meta-data every 60 sec
    kafkaStream.checkpoint(Seconds(60))

    // Actions applied to DStream
    kafkaStream.foreachRDD(rdd => {

      // Transform key value pair rdd to it's value and persist it for optimization during following transformations
      val rddValue = rdd.map(record => record._2).persist()


      // Filter update/insert event-sourcing events and handle them accordingly
      rddValue
        .filter(record =>
          record.contains("event")
            && record.contains("data")
            && !record.contains("request")
            && !record.contains("response")
            && !record.contains("ProfileDeleted")
        )
        .map(EventHandler.handleUpdate(_)).filter(_.isDefined).map(_.get)
        .saveToCassandra("userdb", "profile", SomeColumns("uuid", "name", "email"))

      // Filter delete event-sourcing events and handle them accordingly
      rddValue
        .filter(record =>
          record.contains("event")
            && record.contains("data")
            && !record.contains("request")
            && !record.contains("response")
            && record.contains("ProfileDeleted")
        )
        .map(EventHandler.handleDelete(_)).filter(_.isDefined).map(_.get)
        .foreach(CassandraSession.session.execute(_))


      // Filter application log type data and write it to elastic search (index: fastdata & type: log)
      rddValue
        .filter(record => record.contains("request") || record.contains("response"))
        .saveJsonToEs("fastdata/log")

      // Printing streamed data in console
      println(" <---- Message batch received from kafka ----> ")
      rddValue.foreach(println)

      // Remove persisted rdd but in non-blocking way
      rddValue.unpersist(false)
    }
    )

    // Start streaming & wait indefinite for termination
    ssc.start()
    ssc.awaitTermination()
    CassandraSession.session.close()
    CassandraSession.cluster.close()
  }

}

// spark-cassandra-connector doesn't support the deletion
// hence using datastax driver to create a separate cassandre session
private object CassandraSession {
  val cluster = Cluster.builder().addContactPoint("127.0.0.1").build()
  val session = cluster.connect("userdb")
}