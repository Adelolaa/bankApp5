#Build stage
FROM openjdk:17-jdk-alpine
ARG JAR-FILE=build/*.jar
COPY ./build/libs/*.jar bankApp5.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar","/bankApp5.jar"]


