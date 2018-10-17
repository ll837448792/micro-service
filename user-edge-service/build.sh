#!/bin/bash

mvn package
docker build -t 192.168.1.103:80/micro-service/user-edge-service:latest .

#docker run -ti user-edge-service:latest --redis.address=10.11.239.4
docker push 192.168.1.103:80/micro-service/user-edge-service:latest
