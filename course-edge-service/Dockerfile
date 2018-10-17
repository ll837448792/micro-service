FROM 192.168.1.103:80/micro-service/openjdk:8-jre
MAINTAINER smallsoup smallsoup@163.com

COPY target/course-edge-service-1.0-SNAPSHOT.jar /course-edge-service.jar

ENTRYPOINT ["java", "-jar", "/course-edge-service.jar"]
