# <h1 style="font-size: 36px; margin: 0;">LakeSide - Hotel Booking System 🏨</h1>

A robust RESTful API backend for a hotel room booking platform built with Spring Boot. This server-side application provides secure JWT-based authentication with cookie support, comprehensive room management, and booking functionality, enabling guests to reserve rooms while giving administrators complete control over inventory and reservations.

- [Features](#features-)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Security & Authentication](#security--authentication)
- [Troubleshooting](#troubleshooting)
- [License](#license)

## Features 

- **JWT Authentication with Cookie Support**: Secure token-based authentication using JSON Web Tokens stored in HTTP-only cookies. Custom authentication filter validates tokens from both Authorization headers and AUTH_TOKEN cookies for flexible client integration.
- **Dual Authentication Methods**: Supports both Authorization header (Bearer token) and cookie-based authentication, allowing seamless integration with web frontends and mobile applications.
- **User Account Management**: Registration with email validation using regex patterns, BCrypt password hashing, and secure session management. Automatic JWT token generation on successful login.
- **Room Management API**: Complete CRUD operations for rooms including adding new rooms with photos, updating room details (type, price, photos), and deleting rooms. Photos stored as BLOBs in PostgreSQL with Base64 encoding support.
- **Room Booking System**: Users can book rooms with check-in/check-out dates, guest information (adults and children count), and party size. Automatic confirmation code generation and real-time availability tracking.
- **Booking History**: Maintains complete booking history for each user with status tracking (CONFIRMED, CANCELLED, COMPLETED, NO_SHOW). Users can view their past and current bookings.
- **Photo Handling**: Room photos stored as BLOBs in PostgreSQL database with Base64 encoding for frontend consumption. Transactional support for large object operations and efficient retrieval.
- **Custom Exception Handling**: Comprehensive error handling with custom exceptions for invalid passwords, missing resources, photo retrieval issues, user account errors, and booking conflicts.
- **CORS Configuration**: Properly configured CORS settings with credentials support for seamless integration with frontend applications running on different ports and origins.
- **Layered Architecture**: Clean separation of concerns with controllers, services, and repositories following Spring Boot best practices for maintainability and testability.
- **Role-Based Access Control**: Supports multiple user roles (USER, ADMIN, OWNER) with different permission levels for various operations.

---

## Tech Stack

- **Spring Boot 3.5.4** - Enterprise Java framework for building RESTful APIs with auto-configuration and production-ready features
- **Java 21** - Modern Java features including records, pattern matching, and performance improvements
- **PostgreSQL** - Relational database for persistent data storage with advanced features and reliability
- **Spring Security** - Authentication and authorization framework with custom JWT filter integration
- **JWT (JSON Web Tokens)** - Stateless authentication tokens using jjwt library (v0.12.6) with custom filter implementation
- **JPA/Hibernate** - Object-relational mapping for database operations with automatic schema management
- **Maven** - Dependency management and build automation with Spring Boot Maven plugin
- **Lombok** - Reduces boilerplate code with annotations (@Data, @Builder, etc.)
- **BCrypt** - Secure password hashing algorithm for storing user credentials
- **Jackson** - JSON processing library for API request/response serialization
- **Apache Commons Lang3** - Utility libraries for common operations and helper methods

---

## Project Structure

```
LakeSide/
├── src/main/java/com/LakeSide/LakeSide/
│   ├── controller/                    # REST API endpoints
│   │   ├── UserAccountController.java        # User registration, login, profile
│   │   ├── RoomController.java              # Room CRUD operations, booking
│   │   └── BookingHistoryController.java    # Booking history retrieval
│   ├── service/                      # Business logic layer
│   │   ├── IUserAccountServiceImpl.java     # User account management
│   │   └── Room/
│   │       ├── RoomServiceImpl.java         # Room operations
│   │       └── BookedRoomServiceImpl.java   # Booking operations
│   │   └── RoomBookingsHistory/
│   │       └── RoomBookingsHistoryServiceImpl.java  # Booking history
│   ├── repository/                   # Data access layer (JPA Repositories)
│   │   ├── UserAccountRepository.java
│   │   ├── RoomRepository.java
│   │   ├── BookedRoomRepository.java
│   │   └── RoomBookingHistoryRepository.java
│   ├── model/                        # Entity classes (JPA Entities)
│   │   ├── UserAccount.java         # User entity with roles
│   │   ├── Room.java                # Room entity with photos
│   │   ├── BookedRoom.java          # Active booking entity
│   │   └── RoomBookings.java        # Booking history entity
│   ├── response/                     # DTO classes for API responses
│   │   ├── userAccountResponse.java
│   │   ├── userAccountLogInResponse.java
│   │   ├── roomResponse.java
│   │   ├── bookedRoomResponse.java
│   │   └── roomBookingsResponse.java
│   ├── requests/                     # Request DTO classes
│   │   ├── BookRoomRBody.java
│   │   └── RBookingHistoryRBody.java
│   ├── Exception/                    # Custom exception handlers
│   │   ├── ResourceNotFoundException.java
│   │   ├── InvalidPasswordException.java
│   │   ├── UserAccountNotFoundException.java
│   │   ├── PhotoRetrievalException.java
│   │   ├── RoomIsBookedException.java
│   │   └── InternalServerExeption.java
│   ├── Configuration/                # Spring configuration classes
│   │   ├── SecurityConfig.java       # Spring Security & CORS config
│   │   └── AppConfig.java            # Authentication provider & beans
│   ├── JWT/                          # JWT authentication components
│   │   ├── JWTService.java           # Token generation, validation, cookie management
│   │   └── JWTAuthenticationFilter.java  # Request filter for token validation
│   ├── Enums/
│   │   └── BookingStatus.java        # Booking status enumeration
│   └── LakeSideApplication.java      # Main Spring Boot application class
├── src/main/resources/
│   └── application.properties        # Application configuration
├── pom.xml                           # Maven dependencies and build config
└── ReadMe.md                         # Project documentation
```

### Key Components

- **UserAccountController**: Handles user registration (`/auth/create-account`), login (`/auth/sign-in`), and profile retrieval (`/auth/profile`). Manages JWT cookie generation on successful authentication.
- **RoomController**: Manages room operations including listing available rooms (`/rooms/all-rooms`), adding rooms (`/rooms/add/new-room`), updating rooms (`/rooms/update/room/{roomId}`), deleting rooms, and booking rooms (`/rooms/browse-rooms/booking/{roomId}`).
- **BookingHistoryController**: Provides endpoints for retrieving user booking history (`/my-bookings/all-booked-Rooms`).
- **JWTAuthenticationFilter**: Custom filter that intercepts requests and validates JWT tokens from either Authorization header (Bearer token) or AUTH_TOKEN cookie. Extracts user information and sets Spring Security context.
- **JWTService**: Handles JWT token generation, validation, email extraction, and cookie management (creation, deletion) with proper security attributes (HttpOnly, Secure, SameSite).
- **SecurityConfig**: Configures Spring Security with custom security filter chain, CORS settings, and endpoint authorization rules. Public and protected endpoints are clearly defined.

---

## Installation

### Prerequisites

Before you begin, ensure you have the following installed on your system:

- **Java 21 or higher** - [Download Java](https://www.oracle.com/java/technologies/downloads/#java21)
- **Maven 3.6+** - [Download Maven](https://maven.apache.org/download.cgi) (or use the included Maven wrapper: `mvnw`/`mvnw.cmd`)
- **PostgreSQL 12+** - [Download PostgreSQL](https://www.postgresql.org/download/)
- **Git** - For cloning the repository (optional)
- **IDE** (Optional but recommended) - IntelliJ IDEA, Eclipse, or VS Code with Java extensions

### Step-by-Step Installation

1. **Clone the repository** (or download the project files):
   ```bash
   git clone <repository-url>
   cd LakeSide
   ```

2. **Set up PostgreSQL database**:
   ```bash
   # Create a new database
   psql -U postgres
   CREATE DATABASE LakeSide;
   \q
   ```

3. **Configure database connection**:
   
   Open `src/main/resources/application.properties` and update the following:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/LakeSide
   spring.datasource.username=your_postgres_username
   spring.datasource.password=your_postgres_password
   ```

4. **Generate JWT Secret Key** (if needed):
   
   You can generate a Base64-encoded secret key using:
   ```bash
   # Using OpenSSL (Linux/Mac)
   openssl rand -base64 32
   
   # Or use any online Base64 encoder for a random 32-byte key
   ```
   
   Update `jwt.secret.key` in `application.properties` with your generated key.

5. **Build the project**:
   ```bash
   # Using Maven wrapper (recommended)
   ./mvnw clean install
   
   # Or using system Maven
   mvn clean install
   ```

6. **Run the application**:
   ```bash
   # Using Maven wrapper
   ./mvnw spring-boot:run
   
   # Or using system Maven
   mvn spring-boot:run
   
   # Or run the JAR file directly
   java -jar target/LakeSide-0.0.1-SNAPSHOT.jar
   ```

7. **Verify the installation**:
   
   The application should start on `http://localhost:8080` (default port). You can test it by accessing:
   - `http://localhost:8080/rooms/all-rooms` (should return empty array or room list)
   - `http://localhost:8080/rooms/room-types` (should return room types)

---

## Configuration

### Application Properties

The main configuration file is located at `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/LakeSide
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB

# Development Tools
spring.devtools.restart.enabled=true
spring.devtools.livereload.enabled=true

# JWT Configuration
jwt.secret.key=your_base64_encoded_secret_key_here
jwt.expiration=86400000  # 24 hours in milliseconds
```

### Security Configuration

The security configuration is defined in `SecurityConfig.java`:

- **Public Endpoints**: `/auth/**`, `/rooms/all-rooms`, `/rooms/room-types`, `/rooms/room/{roomId}`
- **Protected Endpoints**: `/rooms/browse-rooms/booking/{roomId}`, `/my-bookings/**`, `/rooms/add/new-room`, `/rooms/update/room/{roomId}`, `/rooms/delete/room/{roomId}`
- **CORS Origins**: `http://localhost:5173`, `http://localhost:3000` (configurable in `SecurityConfig.corsConfigurationSource()`)
- **Session Management**: STATELESS (JWT-based, no server-side sessions)

### Cookie Configuration

JWT tokens are stored in HTTP-only cookies with the following settings (configurable in `JWTService.java`):

- **Cookie Name**: `AUTH_TOKEN`
- **HttpOnly**: `true` (prevents JavaScript access)
- **Secure**: `false` for development (set to `true` for HTTPS production)
- **SameSite**: `Lax` (CSRF protection)
- **Path**: `/` (available for all paths)
- **Max Age**: 604800 seconds (7 days)

**⚠️ Important**: For production deployments with HTTPS, update `JWTService.generateCookie()` and `JWTService.deleteCookie()` to set `setSecure(true)`.

---

## Usage

### Starting the Application

1. **Ensure PostgreSQL is running** and the database is created
2. **Update `application.properties`** with your database credentials
3. **Run the application** using one of the methods described in the Installation section
4. **Verify the application** is running by checking the console output or accessing a public endpoint

### API Testing

You can test the API using:

- **Postman** - Import the endpoints and test with different scenarios
- **cURL** - Command-line tool for HTTP requests
- **Frontend Application** - Connect a React, Vue, or Angular frontend to the API

### Example API Calls

#### Register a New User
```bash
curl -X POST "http://localhost:8080/auth/create-account" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "fullName=John Doe&email=john@example.com&password=SecurePass123"
```

#### Login
```bash
curl -X POST "http://localhost:8080/auth/sign-in" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "email=john@example.com&password=SecurePass123" \
  -c cookies.txt
```

#### Get All Rooms (Public)
```bash
curl -X GET "http://localhost:8080/rooms/all-rooms"
```

#### Book a Room (Authenticated)
```bash
curl -X POST "http://localhost:8080/rooms/browse-rooms/booking/1" \
  -H "Content-Type: application/json" \
  -b cookies.txt \
  -d '{
    "checkInDate": "2024-12-25",
    "checkOutDate": "2024-12-28",
    "numOfAdults": 2,
    "numOfChildren": 1
  }'
```

---

## API Endpoints

### Authentication Endpoints (`/auth`)

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| POST | `/auth/create-account` | Register a new user account | Public |
| POST | `/auth/sign-in` | Login and receive JWT cookie | Public |
| GET | `/auth/profile` | Get current user profile | Authenticated |

### Room Endpoints (`/rooms`)

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| GET | `/rooms/all-rooms` | Get all available rooms | Public |
| GET | `/rooms/room-types` | Get all room types | Public |
| GET | `/rooms/room/{roomId}` | Get room details by ID | Public |
| POST | `/rooms/add/new-room` | Add a new room (admin) | Authenticated |
| PUT | `/rooms/update/room/{roomId}` | Update room details | Authenticated |
| DELETE | `/rooms/delete/room/{roomId}` | Delete a room | Authenticated |
| POST | `/rooms/browse-rooms/booking/{roomId}` | Book a room | Authenticated |

### Booking History Endpoints (`/my-bookings`)

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| GET | `/my-bookings/all-booked-Rooms` | Get user's booking history | Authenticated |

---

## Security & Authentication

### Authentication Flow

1. **User Registration**: User provides email, password, and full name. Email is validated using regex, password is hashed with BCrypt before storage.
2. **User Login**: User provides email and password. System validates credentials and generates a JWT token containing user email, role, and full name as claims.
3. **Token Storage**: JWT token is stored in an HTTP-only cookie (`AUTH_TOKEN`) sent automatically by the browser on subsequent requests.
4. **Request Authentication**: `JWTAuthenticationFilter` intercepts each request:
   - Checks for token in `Authorization` header (Bearer format) OR `AUTH_TOKEN` cookie
   - Validates token signature and expiration
   - Extracts user information and sets Spring Security context
   - Allows or denies access based on authentication status
5. **Protected Endpoints**: Endpoints requiring authentication verify the user's identity from the Security context.

### Token Structure

JWT tokens contain the following claims:
- **Subject (sub)**: User email address
- **Custom Claims**:
  - `role`: User role (USER, ADMIN, OWNER)
  - `fullName`: User's full name
- **Expiration**: 24 hours (configurable via `jwt.expiration`)

### Cookie vs Header Authentication

The application supports both authentication methods:

- **Cookie-based** (Recommended for web frontends):
  - Token stored in HTTP-only cookie
  - Automatically sent by browser
  - More secure (not accessible via JavaScript)
  - Used by default in login response

- **Header-based** (Useful for mobile apps/API clients):
  - Token sent in `Authorization: Bearer <token>` header
  - More control for client applications
  - Requires manual header management

The `JWTAuthenticationFilter` checks both methods, prioritizing Authorization header if present, otherwise checking cookies.

### CORS Configuration

CORS is configured to allow requests from:
- `http://localhost:5173` (Vite/React default)
- `http://localhost:3000` (Create React App/Next.js default)

Credentials (cookies) are enabled for cross-origin requests. To add additional origins, modify `SecurityConfig.corsConfigurationSource()`.

**⚠️ Important**: Do not use wildcard (`*`) for allowed origins when `allowCredentials(true)` is set, as browsers will reject such configurations.

---

## Troubleshooting

### Common Issues

1. **Database Connection Error**:
   - Verify PostgreSQL is running: `pg_isready` or check service status
   - Check database credentials in `application.properties`
   - Ensure database `LakeSide` exists
   - Verify PostgreSQL is listening on default port 5432

2. **JWT Token Validation Fails**:
   - Ensure `jwt.secret.key` is a valid Base64-encoded string
   - Verify token hasn't expired (default: 24 hours)
   - Check that token is being sent correctly (cookie or header)
   - For cookie-based auth, ensure CORS credentials are enabled

3. **403 Forbidden on Protected Endpoints**:
   - Verify JWT token is included in request (check cookie or Authorization header)
   - Ensure token is valid and not expired
   - Check that `JWTAuthenticationFilter` is properly configured
   - Verify endpoint is not excluded from security filter chain

4. **Cookie Not Being Set/Sent**:
   - Check CORS configuration allows credentials
   - Verify cookie settings (Secure flag should be `false` for HTTP, `true` for HTTPS)
   - Ensure frontend includes `credentials: 'include'` in fetch requests
   - Check browser console for CORS errors

5. **Photo Upload/Retrieval Issues**:
   - Verify file size is under 10MB limit
   - Check database has sufficient space for BLOB storage
   - Ensure proper transaction management for large objects
   - Verify Base64 encoding/decoding is working correctly

6. **Port Already in Use**:
   - Change server port in `application.properties`: `server.port=8081`
   - Or stop the process using port 8080

### Getting Help

- Check Spring Boot documentation: https://spring.io/projects/spring-boot
- Review Spring Security documentation: https://spring.io/projects/spring-security
- JWT library documentation: https://github.com/jwtk/jjwt
- PostgreSQL documentation: https://www.postgresql.org/docs/

---

## License

This project is open-source and available under the MIT License.

---

## Notes

- **Database Schema**: The application uses JPA's `ddl-auto=update` mode, which automatically creates/updates database tables. For production, consider using `validate` or manual schema management.
- **Password Security**: Always use strong passwords in production. Consider implementing password strength requirements and password reset functionality.
- **JWT Secret Key**: Never commit your production JWT secret key to version control. Use environment variables or secure configuration management.
- **HTTPS in Production**: Always use HTTPS in production environments and set `Secure=true` for cookies to prevent man-in-the-middle attacks.
- **Rate Limiting**: Consider implementing rate limiting for authentication endpoints to prevent brute-force attacks.
- **Logging**: Review and configure logging levels appropriately for production to avoid exposing sensitive information.
- **Error Messages**: Customize error messages to avoid leaking system information while maintaining useful debugging information.
- **Photo Storage**: For production, consider using cloud storage (AWS S3, Google Cloud Storage) instead of database BLOBs for better performance and scalability.
