#Stage 1:

FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN ./mvnw clean package -DskipTests
#Stage 2:

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
ARG JAR_FILE=target/LibraryManagementSystem-1.0.0.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=prod","-jar", "app.jar"]
