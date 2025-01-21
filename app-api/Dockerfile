FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/quarkus-app /app
EXPOSE 8080
CMD ["java", "-jar", "quarkus-run.jar"]