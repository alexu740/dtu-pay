cd messaging-utilities
mvn clean install
cd ..

cd account-management
mvn clean package
cd ..

cd app-api
mvn clean package
cd ..

cd customer-facade
mvn clean package
cd ..

cd merchant-facade
mvn clean package
cd ..

cd payment-management
mvn clean package
cd ..

cd tokenservice
mvn clean package
cd ..

cd reportservice
mvn clean package
cd ..