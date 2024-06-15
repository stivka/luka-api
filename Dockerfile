#
# Build stage
#
FROM gradle:jdk-21-and-22-graal AS build
WORKDIR /home/app

# Copy the Gradle wrapper and project files
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .
COPY src ./src

# Ensure the Gradle wrapper has execution permissions
RUN chmod +x gradlew

# Build the application
RUN ./gradlew build

#
# Package stage
#
FROM openjdk:22-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /home/app/build/libs/*.jar /app/app.jar

# Specify the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
