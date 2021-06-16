FROM openjdk:8

EXPOSE 8091

ADD target/grade-0.0.1-SNAPSHOT.jar grade-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/grade-0.0.1-SNAPSHOT.jar"]