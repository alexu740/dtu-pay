FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/quarkus-app /app
EXPOSE 8088
CMD ["java", "-jar", "quarkus-run.jar"]