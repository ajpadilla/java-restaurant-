# --- Build Stage ---
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# --- Runtime Stage ---
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy the JAR
COPY --from=build /app/target/*.jar app.jar

# Expose the port
EXPOSE 8081

# Optional: wait for Postgres (recommended)
# COPY wait-for-it.sh /wait-for-it.sh
# ENTRYPOINT ["./wait-for-it.sh", "postgres:5432", "--", "java", "-jar", "app.jar"]


ENTRYPOINT ["java", "-jar", "app.jar"]
