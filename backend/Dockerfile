# syntax=docker/dockerfile:1

FROM gradle:8.3-jdk17 AS build
WORKDIR /app
COPY . /app
RUN gradle build -x test

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/backend-all.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
