FROM maven:3.9.7-sapmachine-22 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:22-jdk-slim
COPY --from=build /target/dao-0.0.1-SNAPSHOT.jar dao.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","dao.jar"]