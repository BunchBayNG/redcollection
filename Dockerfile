FROM maven:3.9.7-sapmachine-22 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim


ENV JAVA_OPTS="--add-opens=java.base/java.lang=ALL-UNNAMED"

COPY target/dao.jar /app/dao.jar


WORKDIR /app


ENTRYPOINT ["java", "$JAVA_OPTS", "-jar", "dao.jar"]
