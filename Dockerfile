#Stage 1:

FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN ./mvnw clean package -DskipTests
#Stage 2:

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/app.jar app.jar


EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=prod","-jar", "app.jar"]
