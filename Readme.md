# Spring Boot Multi-App Docker Setup

This project demonstrates how to run **two Spring Boot applications (`app1` and `app2`)** inside Docker containers and enable **service-to-service communication** without using `localhost`.  

- **App2** provides two endpoints:
  - Health check (`GET /hbt`)
  - Accepts JSON data (`POST /send`) and returns `"Success"`.
- **App1** exposes APIs that internally **call App2’s APIs** using the container hostname instead of `localhost`.  
- Both services are containerized with Docker and orchestrated via Docker Compose.  
- Hostname-based DNS (`app1.local`, `app2.local`) is enabled for calling the services from the host machine/Postman without using `localhost`.

---

## 📂 Project Structure
```
spring-docker-demo/
│── docker-compose.yml
│
├── app1/
│ ├── Dockerfile
│ ├── pom.xml
│ └── src/main/java/com/example/app1/App1Application.java
│ └── src/main/resources/application.properties
│
└── app2/
│ ├── Dockerfile
│ ├── pom.xml
│ └── src/main/java/com/example/app2/App11Application.java
│ └── src/main/resources/application.properties
│
```


---

## 🔹 App2 (Service Provider)

**Endpoints**
- `GET /hbt` → returns `"OK"`
- `POST /send` → accepts JSON request body, logs it, and respond

**Sample URLs**
- Direct: `http://app2.local:8080/hbt`  
- Via localhost (fallback): `http://localhost:8080/hbt`

**Dockerfile (`app2/Dockerfile`):**
```dockerfile
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/app2-0.0.1-SNAPSHOT.jar app2.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app2.jar"]
```
🔹 App1 (Service Caller)
**Endpoints**

-`GET /call-hbt` → calls App2’s /hbt and returns the result

-`POST /call-send` → sends JSON to App2’s /send and returns "Success"

**Sample URLs**

-Direct: `http://app1.local:8081/call-hbt`

-Via localhost (fallback): `http://localhost:8081/call-hbt`

**Dockerfile (`app1/Dockerfile`):**
```
dockerfile
Copy code
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/app1-0.0.1-SNAPSHOT.jar app1.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app1.jar"]
```
🔹 Docker Compose Setup (`docker-compose.yml`)
```
yaml
Copy code
version: "3.8"
services:
  app2:
    build: ./app2
    container_name: app2
    hostname: app2.local
    networks:
      appnet:
        aliases:
          - app2.local
    ports:
      - "8080:8080"

  app1:
    build: ./app1
    container_name: app1
    hostname: app1.local
    networks:
      appnet:
        aliases:
          - app1.local
    ports:
      - "8081:8081"
    depends_on:
      - app2

networks:
  appnet:
    driver: bridge
```
Key Points:
networks.appnet → creates a custom Docker bridge network (appnet) so containers can communicate by hostname.

aliases → assigns DNS names (app1.local, app2.local) inside the Docker network.

depends_on → ensures App2 starts before App1.

ports → map container ports to host machine for external access.

# DNS Mapping for Host Machine

Inside Docker, App1 can call App2 using http://app2.local:8080.
But from the host machine (Postman/curl), the OS does not recognize these names.

👉 To fix this, add the following to your system’s hosts file:
```
127.0.0.1 app1.local
127.0.0.1 app2.local
```
**Linux/Mac → /etc/hosts**

**Windows → C:\Windows\System32\drivers\etc\hosts**

Now you can hit the services from Postman/curl without localhost.

# Build JARs
```
cd app2 && mvn clean package -DskipTests
cd ../app1 && mvn clean package -DskipTests
```
# Start containers
docker-compose up --build
🔹 Test with Curl / Postman
Health Check via App1 → App2
```
curl http://app1.local:8081/call-hbt
curl http://app2.local:8080/hbt
```
 Response: OK

Direct App2 Calls
```
curl http://app2.local:8080/hbt
```
