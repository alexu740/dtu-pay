FROM maven:3.9.9-eclipse-temurin-23-alpine AS build
WORKDIR /app
COPY rest-api/pom.xml .
COPY rest-api/src ./src
RUN mvn clean package -DskipTests
RUN ls -la

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/quarkus-app /app
EXPOSE 8080
CMD ["java", "-jar", "quarkus-run.jar"]