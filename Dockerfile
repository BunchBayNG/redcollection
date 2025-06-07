FROM maven:3.9.7-sapmachine-22 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:22-jdk-slim

# Set JVM options
ENV JAVA_OPTS="--add-opens=java.base/java.lang=ALL-UNNAMED"

# Copy the JAR file into the container
COPY --from=build /target/dao-0.0.1-SNAPSHOT.jar dao.jar

EXPOSE 8080

# Run the application with the JVM options
ENTRYPOINT ["java", "$JAVA_OPTS", "-jar", "dao.jar"]