FROM openjdk:22-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Define the ARG for the JAR file
ARG JAR_FILE=target/*.jar

# Copy the JAR file to the working directory
COPY ${JAR_FILE} app.jar

# Specify the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
