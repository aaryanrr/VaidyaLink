# Stage 1: Build the React app using a Node.js image
FROM node:14-alpine AS build-frontend

# Set working directory
WORKDIR /app/frontend

# Copy the package.json and package-lock.json files
COPY frontend/package*.json ./

# Install dependencies
RUN npm install

# Nodemon to monitor changes and reload
RUN npm install -g nodemon

# Copy the rest of the React app's source code
COPY frontend/ .

# Build the React app for production
RUN npm run build

# Stage 2: Build the Spring Boot app with Java and Gradle
FROM eclipse-temurin:17-jdk-alpine

# Set environment variables for Gradle and Java
ENV GRADLE_VERSION=8.10.1
ENV GRADLE_HOME=/opt/gradle
ENV PATH=${GRADLE_HOME}/bin:${PATH}

# Install required packages: curl, bash, unzip, nodejs, npm
RUN apk update && apk add --no-cache curl bash unzip nodejs npm

# Verify npm and node installation
RUN npm --version && node --version

# Install Gradle manually by downloading from the official Gradle site
RUN mkdir -p /opt/gradle && \
    curl -L https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -o gradle-bin.zip && \
    unzip gradle-bin.zip -d /opt/gradle && \
    rm gradle-bin.zip

# Install MySQL client
RUN apk add --no-cache mysql mysql-client

# Install Ganache CLI for local Ethereum blockchain
RUN npm install -g ganache-cli

# Install Web3j CLI for Ethereum Java interaction
RUN curl -L https://get.web3j.io | sh

# Set working directory
WORKDIR /app

# Copy Gradle wrapper files
COPY gradlew gradlew
COPY gradle/ gradle/

# Copy the Spring Boot project files
COPY build.gradle settings.gradle ./
COPY src/ src/

# Copy the React build files from the previous stage
COPY --from=build-frontend /app/frontend/build src/main/resources/static/

# Run the Gradle build to compile the project without running tests
RUN chmod +x ./gradlew && ./gradlew clean build --no-daemon -x test

# Expose the application, MySQL, and Ganache ports
EXPOSE 8080 3306 8545

# Use JSON format for CMD to handle OS signals correctly
CMD ["sh", "-c", "service mysql start && ganache-cli -p 8545 -h 0.0.0.0 -m 'vaidyalink' & java -jar build/libs/vaidyalink-0.0.1-SNAPSHOT.jar"]
