

#FROM maven:3.8.4-openjdk-17 as build
#WORKDIR /app
#COPY pom.xml .
#COPY src
#S2 run app

#FROM openjdk:17-alpine
#WORKDIR /app
#COPY src ./src


FROM openjdk:17-alpine
WORKDIR /app
LABEL authors="Krisent"
ADD target/UslugiCykliczne-0.2.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","app.jar"]

#docker buildx build --platform linux/amd64 -t bartlomiejskwara/cyclical-services:1.0 .
#docker buildx build --platform windows/amd64 -t bartlomiejskwara/cyclical-services:1.0 .
# docker push bartlomiejskwara/cyclical-services:1.0
#docker run -d -p  8080:80 bartlomiejskwara/cyclical-services:1.0

