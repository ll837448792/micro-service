FROM 192.168.1.103:80/micro-service/openjdk:8-jre

MAINTAINER smallsoup smallsoup@163.com

COPY target/user-edge-service-1.0-SNAPSHOT.jar /user-edge-service.jar

ENTRYPOINT ["java", "-jar", "/user-edge-service.jar"]
