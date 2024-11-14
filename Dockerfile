# Stage 1: Build the application
FROM eclipse-temurin:17-jdk-alpine AS builder

# Set working directory
WORKDIR /app

# Copy maven wrapper and pom.xml
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Download dependencies (cached if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src/ ./src/

# Build the application
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jre-alpine

# Add non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

# Set working directory
WORKDIR /app

# Copy the built artifact from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Change to non-root user
USER spring:spring

# Configure JVM options and timezone
ENV JAVA_OPTS="-Xms512m -Xmx512m" \
    TZ=America/Sao_Paulo

# Health check
HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget -q --spider http://localhost:8080/actuator/health || exit 1

# Expose application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]