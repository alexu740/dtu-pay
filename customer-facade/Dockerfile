FROM maven:3.9.9-eclipse-temurin-23-alpine AS build
WORKDIR /app
COPY customer-facade/pom.xml .
COPY customer-facade/src ./src
RUN mvn clean package -DskipTests


FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/quarkus-app /app
EXPOSE 8088
CMD ["java", "-jar", "quarkus-run.jar"]