# <h1 style="font-size: 36px; margin: 0;">LakeSide Hotel Booking System 🏨</h1>

A robust RESTful API backend for a hotel room booking platform built with Spring Boot. This server-side application provides secure authentication, room management, and booking functionality, enabling guests to reserve rooms while giving administrators complete control over inventory and reservations.

- [Features](#features-)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Usage](#usage)
- [Installation](#installation)
- [License](#license)

## Features 

- **JWT Authentication & Authorization**: Secure token-based authentication with a custom filter that validates requests and extracts user information. Supports three role levels (USER, ADMIN, OWNER) with role-based access control.
- **User Account Management**: Registration with email validation using regex patterns, password hashing with BCrypt, and secure session management.
- **Room Management API**: Complete CRUD operations for rooms including adding new rooms, updating details (type, price, photos), and deleting rooms. Photos stored as BLOBs in PostgreSQL.
- **Room Booking System**: Users can book rooms with check-in/check-out dates, guest information, and party size. Automatic confirmation code generation and real-time availability tracking.
- **Photo Handling**: Room photos stored as BLOBs in the database with Base64 encoding for frontend consumption. Transactional support for large object operations.
- **Custom Exception Handling**: Comprehensive error handling with custom exceptions for invalid passwords, missing resources, photo retrieval issues, and user account errors.
- **CORS Configuration**: Configured to work seamlessly with frontend applications running on different ports and origins.
- **Layered Architecture**: Clean separation of concerns with controllers, services, and repositories following best practices for maintainability and testability.

---

## Tech Stack

- **Spring Boot 3.5.4** - Enterprise Java framework for building RESTful APIs
- **Java 21** - Modern Java features and performance improvements
- **PostgreSQL** - Relational database for persistent data storage
- **Spring Security** - Authentication and authorization framework
- **JWT (JSON Web Tokens)** - Stateless authentication tokens with custom filter implementation
- **JPA/Hibernate** - Object-relational mapping for database operations
- **Maven** - Dependency management and build automation
- **Lombok** - Reduces boilerplate code with annotations
- **BCrypt** - Secure password hashing algorithm
- **Jackson** - JSON processing for API responses
- **Apache Commons Lang3** - Utility libraries for common operations

---

## Project Structure

```
src/main/java/com/LakeSide/LakeSide/
├── controller/          # REST API endpoints
│   ├── RoomController.java
│   ├── BookedRoomController.java
│   └── UserAccountController.java
├── service/            # Business logic layer
│   ├── IUserAccountServiceImpl.java
│   └── Room/
│       ├── RoomServiceImpl.java
│       └── BookedRoomServiceImpl.java
├── repository/         # Data access layer
│   ├── RoomRepository.java
│   ├── BookedRoomRepository.java
│   └── UserAccountRepository.java
├── model/              # Entity classes
│   ├── Room.java
│   ├── BookedRoom.java
│   └── UserAccount.java
├── response/           # DTO classes for API responses
│   ├── roomResponse.java
│   ├── bookedRoomResponse.java
│   └── userAccountResponse.java
├── Exception/          # Custom exception handlers
│   ├── ResourceNotFoundException.java
│   ├── InvalidPasswordException.java
│   └── UserAccountNotFoundException.java
└── Configuration/      # Security and app configuration
    ├── SecurityConfig.java
    └── AppConfig.java
JWT/
├── JWTService.java           # Token generation and validation
└── JWTAuthenticationFilter.java  # Request interceptor
```

---


### Authentication Flow
1. User registers with email, password, and full name
2. Email is validated using regex pattern
3. Password is hashed with BCrypt before storage
4. On login, JWT token is generated with user role and info as claims
5. Token is included in Authorization header for protected endpoints
6. Custom filter validates token on each request

### Security
- Public endpoints: `/auth/**`, `/rooms/all-rooms`
- Protected endpoints: `/rooms/bookings/**`, `/rooms/browse-rooms/booking/**`
- Role-based access: Different permissions for USER, ADMIN, and OWNER roles
- CORS enabled for frontend integration on localhost:5173 and localhost:3000

---

## Installation

```bash
# Prerequisites: Java 21, Maven, PostgreSQL

# Clone the repository
git clone https://github.com/YourUsername/LakeSide-Backend.git

# Navigate into the project directory
cd LakeSide-Backend

# Configure database connection in application.properties
# Update spring.datasource.url, username, and password

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run

# Or run the JAR file
java -jar target/LakeSide-0.0.1-SNAPSHOT.jar
```

### Configuration

Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/LakeSide
spring.datasource.username=your_username
spring.datasource.password=your_password
jwt.secret.key=your_base64_encoded_secret_key
jwt.expiration=86400000
```

---

## License

This project is open-source and available under the MIT License.

---
