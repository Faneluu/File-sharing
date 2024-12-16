FROM openjdk:latest

WORKDIR /app

COPY target/file-sharing-backend-0.0.1-SNAPSHOT.jar /app/file-sharing-backend-0.0.1-SNAPSHOT.jar
COPY .env /app/.env

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "file-sharing-backend-0.0.1-SNAPSHOT.jar"]
