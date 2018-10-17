#!/bin/bash
docker build -t 192.168.1.103:80/micro-service/python-base:latest -f Dockerfile.base .
docker push 192.168.1.103:80/micro-service/python-base:latest
