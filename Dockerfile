FROM maven:3.9.7-sapmachine-22 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

# Set JVM options
ENV JAVA_OPTS="--add-opens=java.base/java.lang=ALL-UNNAMED"

# Copy the JAR file into the container
COPY target/dao.jar /app/dao.jar

# Set the working directory
WORKDIR /app

# Run the application with the JVM options
ENTRYPOINT ["java", "$JAVA_OPTS", "-jar", "dao.jar"]
