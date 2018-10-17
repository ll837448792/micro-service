FROM 192.168.1.103:80/micro-service/openjdk:8-jre
MAINTAINER smallsoup smallsoup@163.com

COPY target/course-dubbo-service-1.0-SNAPSHOT.jar /course-dubbo-service.jar

ENTRYPOINT ["java", "-jar", "/course-dubbo-service.jar"]
