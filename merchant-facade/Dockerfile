FROM maven:3.9.9-eclipse-temurin-23-alpine AS build
WORKDIR /app
COPY merchant-facade/pom.xml .
COPY merchant-facade/src ./src
RUN mvn clean package -DskipTests


FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/quarkus-app /app
EXPOSE 8089
CMD ["java", "-jar", "quarkus-run.jar"]