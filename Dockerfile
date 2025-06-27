FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml /app/
COPY .mvn /app/.mvn
COPY mvnw /app/mvnw

RUN chmod +x /app/mvnw

RUN --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline -B

COPY src /app/src

RUN ./mvnw clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
