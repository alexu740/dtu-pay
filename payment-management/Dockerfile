FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target /app
CMD ["java", "-jar", "dtupay_paymentmanagement-1.0.jar"]