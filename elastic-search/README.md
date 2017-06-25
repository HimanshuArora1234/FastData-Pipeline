





# Elastic search in docker

## How to install 

First step will be to pull the docker image of elastic search as follows

```
sudo docker pull elasticsearch
```

## How to run

Create a directory to hold the persisted index data

```
mkdir esdata
```

Run a Docker container, bind the esdata directory (volume) and expose port 9200

```
sudo docker run -d --name elasticsearch -v "$PWD/esdata":/usr/share/elasticsearch/data -p 9200:9200 elasticsearch
```

## How to test

To test, point your browser at port 9200 http://localhost:9200 and you should see the following output in your browser

```
{
  "name" : "eWm5aBL",
  "cluster_name" : "elasticsearch",
  "cluster_uuid" : "A-R5XjMTTe6ub0rPXKS2ew",
  "version" : {
    "number" : "5.3.0",
    "build_hash" : "3adb13b",
    "build_date" : "2017-03-23T03:31:50.652Z",
    "build_snapshot" : false,
    "lucene_version" : "6.4.1"
  },
  "tagline" : "You Know, for Search"
}
```



--------------------------------------------------------------------------



## Tweaks on Mac book



I had some trouble running the latest version of ES container on Mac book. It kept stopping itself. So I had to pull an older version and run it with some extra options. Again I faced this problem only with Mac book otherwise the latest version worked fine on linux.



```
docker pull elasticsearch:2.4
```



```
docker run -d -p 9200:9200 --name elasticsearch -e "bootstrap_memory_lock=true" --ulimit memlock=-1:-1 -v "$PWD/esdata":/usr/share/elasticsearch/data -e "http.host=0.0.0.0" -e "transport.host=127.0.0.1" elasticsearch:2.4
```