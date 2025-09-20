FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/app2-0.0.1-SNAPSHOT.jar app2.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app2.jar"]
