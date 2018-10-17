#!/bin/bash

mvn package

docker build -t 192.168.1.103:80/micro-service/api-gateway-zuul:latest -f Dockerfile .

#docker run -ti api-gateway-zuul:latest

docker push 192.168.1.103:80/micro-service/api-gateway-zuul:latest
