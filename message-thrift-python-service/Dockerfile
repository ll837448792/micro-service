FROM 192.168.1.103:80/micro-service/python-base:latest

MAINTAINER smallsoup smallsoup@163.com
ENV PYTHONPATH /
COPY message /message

ENTRYPOINT ["python", "/message/message_service.py"]
