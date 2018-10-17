#!/bin/bash

docker build -t 192.168.1.103:80/micro-service/message-thrift-python-service:latest -f Dockerfile .
#docker run -ti message-thrift-python-service:latest bash
docker push 192.168.1.103:80/micro-service/message-thrift-python-service:latest
