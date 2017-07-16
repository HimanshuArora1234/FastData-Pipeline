package factory.cassandraFactory

import com.datastax.driver.core._
import play.api.libs.json.{JsValue, Json}


/**
  * Helper object to create cassandra session and run the queries.
  */
object CassandraHelper {

  val cluster = Cluster.builder().addContactPoint("127.0.0.1").build()
  val session = cluster.connect("userdb")

  def getAll: ResultSet = session.execute("SELECT * FROM profile;")

  def rowToJson(row: Row): JsValue = Json.obj(
    "uuid" -> row.getString("uuid"),
    "name" -> row.getString("name"),
    "email" -> row.getString("email")
  )

}
