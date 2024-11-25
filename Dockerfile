# Stage 1: Build the JAR file
FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /app

# Install Maven
RUN apk add --no-cache maven

# Copy project files
COPY pom.xml ./
COPY src ./src

# Run the Maven build
RUN mvn -B clean package -DskipTests -P local

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Debug: List files in the directory to check if app.jar exists
RUN ls -la /app

# Ensure correct permissions for the JAR
RUN chmod +x /app/app.jar

# Expose the port and set the entry point
EXPOSE 8484
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
