# Prime Numbers Web Client CLI

This is a **Command-Line Interface (CLI)** application for checking if numbers are prime, leveraging a remote REST API.
Built with **Spring Boot**, it supports:

- Retrieving prime status from a server.
- Storing new prime checks on the server if the number isn't already cached.

## Features

- Check if a number is prime via a REST API.
- Automatically compute and store results for uncached numbers.
- Simple and intuitive CLI interface.

## Prerequisites

- Java 21
- Maven
- A running instance of the Prime Numbers API (configured in `application.properties`).

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/yourusername/prime-numbers-web-client.git
cd prime-numbers-web-client
```

## Configure the Application

Set the server URL in src/main/resources/application.properties:

```text
server.url=http://your-server-url
```

## Build and Run

```bash
mvn clean package
java -Dserver.port=8080 -Dcluster.servers=http://localhost:8081,http://localhost:8082 -jar server.jar
java -Dserver.port=8081 -Dcluster.servers=http://localhost:8080,http://localhost:8082 -jar server.jar
java -Dserver.port=8082 -Dcluster.servers=http://localhost:8080,http://localhost:8081 -jar server.jar
```

## Usage

Once running, the CLI accepts inputs to check if a number is prime:

```bash
shell:> is-prime 7
```

If the number is not cached, the application will automatically compute and store the result on the server.
You are then free to check the number again.