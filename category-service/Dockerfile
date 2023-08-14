FROM openjdk:17-jdk-alpine
LABEL authors="melih"
ARG JAR_FILE=target/*.jar
COPY ./target/category-service-0.0.1.jar app.jar
ENTRYPOINT ["java", "-jar","/app.jar"]