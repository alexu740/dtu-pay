FROM openjdk:17-jdk-slim
COPY target/quarkus-app /app
WORKDIR /app
EXPOSE 8080
CMD ["java", "-jar", "quarkus-run.jar"]