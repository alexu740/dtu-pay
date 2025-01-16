cd ../messaging-utilities

mvn clean package
cd ../merchant-facade
mvn clean package
#java -jar target/dtupay_merchantfacade-1.0.jar

java -jar target/quarkus-app/quarkus-run.jar