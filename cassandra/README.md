# Cassandra in docker

## How to install 

First step will be to pull the docker image of cassandra as follows

```
sudo docker pull oscerd/cassandra:cassandra-2.1.6
```

## How to run

```
docker run --name cassandra -p 7000:7000 -p 7001:7001 -p 7199:7199 -p 9042:9042 -p 9160:9160 -v /my/own/datadir:/var/lib/cassandra -dt oscerd/cassandra:cassandra-2.1.6
```

`/my/own/datadir` is the path to directory of your machine where you want cassandra container to persist the data.


## How to test

Either use the locally installed Cassandra’s Cqlsh (under /bin folder) or use the following command to start another Cassandra container instance and run cqlsh (Cassandra Query Language Shell) against your original Cassandra container, allowing you to execute CQL statements against your database instance. 

```
sudo docker run -it --link cassandra:cassandra --rm cassandra cqlsh cassandra
```

So if you see the following output as a result that means your cassandra container is up & running:

![cqlsh](cqlsh.png)



## Useful Cql commands

```
CREATE KEYSPACE userdb WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

USE userdb;


CREATE TABLE profile (   uuid text PRIMARY KEY,   name text,   email text );

SELECT * FROM profile;
```

## Compatibility check

Make sure to use the mutually compatible versions of spare, Cassandra server i.e. docker image, Cassandra spark connector and Cassandra core drive. This is very important to use the compatible versions otherwise it’s going to be real tough to make things work. Refer the link below:

https://github.com/datastax/spark-cassandra-connector