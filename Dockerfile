# Use Java 17 base image
FROM openjdk:17-jdk-slim

# Create a non-root user for security best practices
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Set working directory
WORKDIR /app

# Copy the JAR file from Maven target directory
COPY target/simple-inventory-1.0.0.jar app.jar

# Change ownership to non-root user
RUN chown appuser:appuser app.jar

# Switch to non-root user
USER appuser

# Expose port (not really needed for command-line app, but good practice)
EXPOSE 8080

# Run the application with optimized JVM settings
CMD ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
