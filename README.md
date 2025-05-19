# Spring Web Project

This is a simple Spring web application that demonstrates the use of Spring MVC with XML configuration.

## Project Structure

```
spring-web-project
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           ├── controller
│   │   │           │   └── HomeController.java
│   │   │           ├── model
│   │   │           │   └── User.java
│   │   │           └── service
│   │   │               └── UserService.java
│   │   └── resources
│   │       ├── applicationContext.xml
│   │       └── dispatcher-servlet.xml
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── controller
│                       └── HomeControllerTest.java
├── pom.xml
└── README.md
```

## Features

- **HomeController**: Handles HTTP GET requests for the home page.
- **User Model**: Represents a user with properties like id, name, and email.
- **UserService**: Provides user-related operations such as retrieving and creating users.
- **Spring Configuration**: Uses XML files for application context and Spring MVC configuration.

## Getting Started

1. Clone the repository.
2. Navigate to the project directory.
3. Build the project using Maven: `mvn clean install`.
4. Run the application on a server (e.g., Tomcat).

## Testing

Unit tests for the `HomeController` are located in the `src/test/java/com/example/controller/HomeControllerTest.java` file. Use Maven to run the tests: `mvn test`.

## Dependencies

This project uses Maven for dependency management. Check the `pom.xml` file for the list of dependencies required for the project.