FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/94-url-shortener-scale-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]