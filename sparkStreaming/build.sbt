name := "streamingApp"

version := "1.0"

val sparkVersion = "1.4.1"
scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.6.0" exclude("com.fasterxml.jackson.core", "jackson-databind"),  // using only to easily parse the events sent as Json
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "org.apache.spark" %% "spark-streaming-kafka" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.12",
  "com.datastax.spark" %% "spark-cassandra-connector" % "1.4.5",
  "com.datastax.cassandra" % "cassandra-driver-core" % "2.1",
  "org.elasticsearch" %% "elasticsearch-spark-13" % "5.3.0" excludeAll (
    ExclusionRule(organization = "org.apache.spark"),
    ExclusionRule(organization = "org.apache.hadoop"),
    ExclusionRule(organization = "org.eclipse.jetty.orbit"),
    ExclusionRule(organization = "com.google.guava"),
    ExclusionRule(organization = "org.slf4j"))
)

