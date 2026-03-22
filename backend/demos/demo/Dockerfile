# Use Java base image
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Copy jar file
COPY target/*.jar app.jar

# Run application
CMD ["java", "-jar", "app.jar"]
