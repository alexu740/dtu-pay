FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target /app
CMD ["java", "-jar", "reportservice-1.0.0.jar"]