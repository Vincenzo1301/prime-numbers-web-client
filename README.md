# Prime Numbers Web Client CLI

This is a **Command-Line Interface (CLI)** application for checking if numbers are prime, leveraging a remote REST API. Built with **Spring Boot**, it supports:
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
java -jar target/prime-numbers-web-client-0.0.1-SNAPSHOT.jar
```

## Usage

Once running, the CLI accepts inputs to check if a number is prime:

```bash
Enter a number to check if it is prime: 7
Number 7 is prime.
```

If the number is not cached, the application will automatically compute and store the result on the server:

```bash
Enter a number to check if it is prime: 8
Number is stored successfully. You can try it again.

Enter a number to check if it is prime: 8
Number 8 is not prime.
```
