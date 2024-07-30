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

# Build the application without running tests
RUN ./gradlew build -x test

#
# Package stage
#
FROM openjdk:22-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Set the timezone to UTC
ENV TZ=UTC
# Install tzdata package
RUN apt-get update && apt-get install -y tzdata

# Copy the built JAR file from the build stage
COPY --from=build /home/app/build/libs/*.jar /app/app.jar
EXPOSE $PORT
# Specify the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
