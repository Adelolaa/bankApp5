
FROM maven:3.6.0-jdk-11-slim3.8.3-openjdk-17 AS build
COPY . .
RUN mvn clean package

FROM openjdk:19-alpine
COPY --from=build target/bankApp5-0.0.1-SNAPSHOT.jar bankApp5.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar","/bankApp5.jar"]



