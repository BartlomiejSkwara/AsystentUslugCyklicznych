FROM openjdk:17
LABEL authors="Krisent"


ADD target/UslugiCykliczne-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar","/app.jar"]

