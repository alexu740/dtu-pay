FROM eclipse-temurin:21 as jre-build
COPY target/lib /usr/src/lib
COPY target/reportservice-1.0.0.jar /usr/src/
WORKDIR /usr/src/
CMD java -Xmx64m -jar reportservice-1.0.0.jar
