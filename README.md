
# 🚀 Pilot Booking System - Core API (Backend)

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=flat-square&logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14%2B-316192?style=flat-square&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-6.x-DC382D?style=flat-square&logo=redis&logoColor=white)](https://redis.io/)
[![Flyway](https://img.shields.io/badge/Flyway-Database%20Migration-CC0200?style=flat-square&logo=flyway&logoColor=white)](https://flywaydb.org/)

Enterprise-grade RESTful API engine for the **Pilot Booking System**. Built with a robust Java Spring Boot architecture, providing secure, scalable, and high-performance endpoints to manage bookings, services, metrics, and file storage.

## ✨ Key Features

* **Advanced Authentication:** JWT-based stateless authentication with Role-Based Access Control (RBAC).
* **Dynamic Booking Engine:** Supports multi-step booking workflows (Soft Booking & Hard Booking) with dynamic date availability checks.
* **Multipart File Handling:** Secure local storage integration for service catalog images and payment proofs.
* **Dashboard Metrics Analytics:** Real-time aggregation of revenue, active services, and booking statuses.
* **Database Versioning:** Automated schema migrations and data seeding using Flyway.
* **Interactive API Docs:** Auto-generated OpenAPI (Swagger UI) documentation.

## 🛠️ Technology Stack

* **Core:** Java 17, Spring Boot, Spring Data JPA, Spring Security
* **Database:** PostgreSQL (Primary Data), Redis (Caching & Fast Lookups)
* **Migration:** Flyway
* **Documentation:** Springdoc OpenAPI (Swagger UI)
* **Build Tool:** Maven

## ⚙️ Prerequisites

Ensure you have the following installed on your local machine:
* [Java 17+](https://adoptium.net/)
* [Maven](https://maven.apache.org/)
* [PostgreSQL](https://www.postgresql.org/)
* [Redis](https://redis.io/) (Optional but recommended for full feature parity)

## 🚀 Getting Started

### 1. Database Setup
Create a new PostgreSQL database:
```sql
CREATE DATABASE pilot_booking_db;
2. Environment Configuration

Update the src/main/resources/application.properties or application.yml file with your credentials:

Properties
spring.datasource.url=jdbc:postgresql://localhost:5432/pilot_booking_db
spring.datasource.username=your_postgres_user
spring.datasource.password=your_postgres_password

# JWT Secret Key (Generate a strong Base64 key)
app.jwt.secret=your_super_secret_jwt_key_here
app.jwt.expiration=900000 # 15 minutes
3. Build and Run

Clone the repository and run the application using Maven:

Bash
# Clean and compile the project
./mvnw clean install

# Run the Spring Boot application
./mvnw spring-boot:run
4. API Documentation

Once the server is running (default port 8080), access the interactive API documentation at:
👉 http://localhost:8080/swagger-ui/index.html

📁 Directory Structure
Plaintext
src/main/java/com/pilotbooking/
├── config/         # Spring Security, WebMvc, Swagger configs
├── controller/     # REST API Endpoints (Admin, Public, Auth, Booking)
├── domain/         # JPA Entities
├── repository/     # Spring Data JPA Repositories
├── security/       # JWT Filters and Authentication Logic
├── service/        # Business Logic & File Storage interfaces/impls
└── web/dto/        # Data Transfer Objects (Request/Response)