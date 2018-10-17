#!/bin/bash

mvn package
docker build -t 192.168.1.103:80/micro-service/user-thrift-service:latest .

#docker run -ti user-thrift-service:latest --mysql.address=10.11.239.4
docker push 192.168.1.103:80/micro-service/user-thrift-service:latest
