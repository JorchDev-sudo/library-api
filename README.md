📚 Library API
















📌 Overview

Library API is a production-oriented REST API built with Spring Boot for managing a digital library, including authors, books, users, and loans, with JWT-based authentication, role-based access control, and fully documented endpoints via Swagger/OpenAPI.

This project is part of my backend portfolio and showcases how I design secure, maintainable, and testable APIs using the modern Spring ecosystem.

🚀 Key Features

🔐 Stateless authentication & authorization with JWT

🧑‍💻 Full management of:

Authors
Books
Users
Loans

🔗 Domain relationships:

Author ↔ Book (Many-to-Many)

User → Loan (One-to-Many)

Book → Loan (One-to-Many)

📄 Pagination and sorting

🧾 DTO-based API design using MapStruct

🧠 Centralized global exception handling

📑 Interactive API documentation (Swagger / OpenAPI)

🧪 Unit and security tests

🛠 Database migrations with Flyway

🌍 Environment-based configuration (dev / prod)

🏗️ Architecture:

The application follows a clear layered architecture, inspired by real-world Spring Boot backend projects:

client
 └── controllers
 └── security

server
 └── services
 └── dto
 └── mappers
 └── exceptions
 └── handlers

persistence
 └── entities
 └── repositories


✔ Strong separation of concerns
✔ Controllers kept thin
✔ Business logic isolated in services
✔ Clean mapping between domain and API models

🛠️ Tech Stack:

Language & Platform:

Java 17

Spring Boot 3.2.5

Spring Ecosystem

Spring Web

Spring Data JPA

Spring Security

Security

JWT authentication

Custom JwtAuthenticationFilter

Custom UserDetailsService

Stateless security configuration

Persistence

Hibernate

H2 (development)

PostgreSQL (production)

Flyway migrations

Tooling & Quality:

MapStruct

Swagger / OpenAPI (springdoc)

JUnit 5

Mockito

Maven

🔐 Security Design

JWT-based stateless authentication

Custom security filter chain

Role-based endpoint protection

Custom UserDetailsService

Proper HTTP status handling:

401 Unauthorized

403 Forbidden

Centralized security exception handling

This setup closely mirrors enterprise-grade Spring Security configurations.

📑 API Documentation

Swagger UI is available at:

http://localhost:8080/swagger-ui.html


or

http://localhost:8080/swagger-ui/index.html

⚙️ Running the Application:

1️⃣ Clone the repository
git clone https://github.com/JorchDev-sudo/library-api.git
cd library-api

2️⃣ Required Environment Variables
JWT_SECRET=your_secret_key


You can configure this via:

System environment variables

IDE Configurations

Deployment environment

3️⃣ Run in development mode
mvn spring-boot:run

Default configuration:

Database: H2

Active profile: dev

🌍 Spring Profiles:

dev	H2 (in-memory)
prod	PostgreSQL + Flyway

Activate a profile using:

spring.profiles.active=dev

🧪 Testing Strategy

Unit tests for:

Services

Mappers

Security components

Security tests for:

JWT validation

Authentication filters

Access restrictions

Mockito used for controlled isolation of dependencies

Testing is focused on business rules, security correctness, and mapping reliability.

This project reflects how I structure and build maintainable backend systems beyond simple CRUD demos.

📬 Contact

If you’d like to discuss this project or my backend experience:

💼 LinkedIn: www.linkedin.com/in/jorge-cotera-lópez-24180438a

📧 Email: jorgecoteralopez@gmail.com
