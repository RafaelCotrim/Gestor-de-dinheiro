FROM openjdk:17
COPY money.mannager.api/target/money.mannager.api-0.0.1-SNAPSHOT.jar api-server.jar
ENTRYPOINT ["java","-jar","api-server.jar"]

# docker build -t api-server:latest . 
# docker run -p 8080:8080 api-server:latest 