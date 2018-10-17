#!/bin/bash

mvn package

docker build -t 192.168.1.103:80/micro-service/course-edge-service:latest -f Dockerfile .

#docker run -ti course-edge-service:latest --zookeeper.address=10.11.239.4
docker push 192.168.1.103:80/micro-service/course-edge-service:latest
