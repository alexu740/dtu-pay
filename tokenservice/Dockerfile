FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target /app
CMD ["java", "-jar", "tokenservice-1.0.0.jar"]
