# Spring Boot Google Cloud Integration

This project is a simple Spring Boot application that integrates with Google Cloud Storage and Google Cloud Messaging. It uses Google Cloud Storage to store files and Google Cloud Messaging to send notifications to other services.

## Features

- Upload files to Google Cloud Storage
- Send notifications to other services using Google Cloud Messaging

## Architecture

The application is built using Spring Boot and consists of several layers:

- `Controller`: Handles incoming requests and maps them to the appropriate service methods
- `Service`: Contains the business logic for managing files and sending notifications
- `Repository`: Provides an abstraction for interacting with Google Cloud Storage
- `Entity`: Contains the entity classes for the application
- `DTO`: Contains the DTO classes for the application
- `Mapper`: Provides an abstraction for mapping between entity and DTO classes
- `Exception`: Contains the exception classes for the application

## Database

The project uses H2 in-memory database for database storage. The database is initialized with sample data on startup.

## Getting Started

### Prerequisites

- Java 21
- Maven 3.8.1
- Google Cloud Storage
- Google Cloud Messaging

### Installation

Clone the repository

```sh
git clone https://github.com/AthirsonSilva/google-cloud-integration
```

Build the project

```sh
cd google-cloud-integration
mvn clean install
```

Run the application

```sh
java -jar target/google-cloud-integration-0.0.1-SNAPSHOT.jar
```

## Contributing

Contributions are welcome! Just fork the project and make a pull request.

## License

This project is licensed under the MIT License. See the LICENSE file for details.