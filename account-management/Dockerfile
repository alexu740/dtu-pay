FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target /app
CMD ["java", "-jar", "dtupay_accountmanagement-1.0.jar"]