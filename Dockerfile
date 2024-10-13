


#S2 run app

#FROM openjdk:17-alpine
#WORKDIR /app
#COPY src ./src


FROM openjdk:17-alpine
WORKDIR /app

LABEL authors="Krisent"


ADD target/UslugiCykliczne-0.1.0.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar","/app.jar"]

#docker buildx build --platform linux/amd64 -t bartlomiejskwara/cyclical-services:1.0 .
#docker buildx build --platform windows/amd64 -t bartlomiejskwara/cyclical-services:1.0 .