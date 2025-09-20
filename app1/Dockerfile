FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/app1-0.0.1-SNAPSHOT.jar app1.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app1.jar"]
