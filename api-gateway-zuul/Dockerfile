FROM 192.168.1.103:80/micro-service/openjdk:8-jre
MAINTAINER smallsoup smallsoup@163.com

COPY target/api-gateway-zuul-1.0-SNAPSHOT.jar /api-gateway-zuul.jar

ENTRYPOINT ["java", "-jar", "/api-gateway-zuul.jar"]
