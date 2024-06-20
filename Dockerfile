#Build stage
#FROM maven:3.6.0-jdk-11-slim AS build
#COPY . .
#RUN mvn clean package -Dmaven.test.skip=true
#
#FROM openjdk:19
#COPY --from=build target/bankApp5-0.0.1-SNAPSHOT.jar bankApp5.jar

FROM openjdk:17-jdk-alpine
ARG JAR-FILE=build/*.jar
COPY --from=build target/bankApp5-0.0.1-SNAPSHOT.jar bankApp5.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar","/bankApp5.jar"]


