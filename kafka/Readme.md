# KAFKA in docker

## Prerequisite

You have docker engine up and running. And a JDK (JDK8 preferred) properly installed.

## Download zookeeper & kafka images

```
docker pull wurstmeister/zookeeper

docker pull wurstmeister/kafka
```

## Verify downloads

```
docker images
```

## Run the containers

```
docker run --name zookeeper -p 2181:2181 -t wurstmeister/zookeeper

docker run --name kafka -e HOST_IP=127.0.0.1 -e KAFKA_ADVERTISED_HOST_NAME=127.0.0.1 -e KAFKA_ADVERTISED_PORT=9092 -e KAFKA_BROKER_ID=1 -e ZK=zk -p 9092:9092 --link zookeeper:zk -t wurstmeister/kafka

```

## Verify the running containers

```
docker ps
```

## Create topic 

let's create a topic named *log*.

```
Z_IP=$(sudo docker inspect --format '{{ .NetworkSettings.IPAddress }}' zookeeper)
docker exec -it 3f223cdc3fcd  kafka-topics.sh --create --topic log --replication-factor 1 --partitions 1 --zookeeper $Z_IP:2181
```

*3f223cdc3fcd* is the container id of kafka, can be obtained by *docker ps*
 command. So just replace it with yours. And you have got yourself a kafka topic.
 
## Create producer & consumer
 
 Just for the sake of certainty that we have our kafka up & running let's create console producer and consumer on log topic and exchange some messages.
 
 ```
 docker exec -it 3f223cdc3fcd kafka-console-producer.sh --topic log --broker-list localhost:9092
 </br>
 docker exec -it 3f223cdc3fcd kafka-console-consumer.sh --topic log --from-beginning --bootstrap-server localhost:9092
 ```

## Another way to test your kafka installation 

Install `KafkaCat` from https://github.com/edenhill/kafkacat and run the following commands to create producer and consumer respectively

 ```
 kafkacat -P -b 127.0.0.1:9092 -t log
 </br>
 kafkacat -C -b 127.0.0.1:9092 -t log 
 ```

 
