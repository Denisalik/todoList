FROM maven:3.9-eclipse-temurin-25 AS deps
WORKDIR /app

COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -B -q dependency:go-offline

FROM deps AS test
WORKDIR /app
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B clean verify

FROM deps AS build
WORKDIR /app
COPY src/main ./src/main
RUN --mount=type=cache,target=/root/.m2 mvn -B -q clean package -DskipTests

FROM eclipse-temurin:25.0.4_7-jdk-jammy as prod
WORKDIR /app

RUN groupadd -r app && useradd -r -g app app
USER app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM deps AS build-without-loki
WORKDIR /app
COPY src/main ./src/main
RUN rm /app/src/main/resources/logback-spring.xml
RUN --mount=type=cache,target=/root/.m2 mvn -B -q clean package -DskipTests

FROM deps AS prod-without-loki
WORKDIR /app
COPY src/main ./src/main
RUN rm /app/src/main/resources/logback-spring.xml
RUN --mount=type=cache,target=/root/.m2 mvn -B -q clean package -DskipTests