# Stage 1: build
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
# cache dependencies
RUN mvn dependency:go-offline -B
COPY src src
RUN mvn package -DskipTests -B

# Stage 2: run
FROM eclipse-temurin:17-jre
WORKDIR /app
# copy jar
COPY --from=build /app/target/antifraude-backend-*.jar app.jar

# expose port
EXPOSE 8080

# entrypoint
ENTRYPOINT ["java","-jar","app.jar"]