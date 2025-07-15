FROM gradle:8.10-jdk21 AS builder
WORKDIR /app

COPY . .
RUN ./gradlew clean build --no-daemon

FROM openjdk:21-jdk-slim
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]