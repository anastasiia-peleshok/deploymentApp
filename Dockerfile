FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/deployment-0.0.1-SNAPSHOT.jar /app/target/deployment-0.0.1-SNAPSHOT.jar

EXPOSE 6969

CMD ["java", "-Dspring.profiles.active=${ENV_TYPE}", "-jar", "/app/target/deployment-0.0.1-SNAPSHOT.jar"]
