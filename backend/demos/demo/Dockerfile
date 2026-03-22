# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app
COPY . .

# Use environment variables in Docker build (if needed)
ARG GEMINI_API_KEY
ARG EMAIL_PASSWORD
ARG DB_PASSWORD

RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copy built jar
COPY --from=builder /app/target/*.jar app.jar

# Pass env variables at runtime (docker run or in Render/Heroku dashboard)
ENV GEMINI_API_KEY=${GEMINI_API_KEY}
ENV EMAIL_PASSWORD=${EMAIL_PASSWORD}
ENV DB_PASSWORD=${DB_PASSWORD}

CMD ["java", "-jar", "app.jar"]
