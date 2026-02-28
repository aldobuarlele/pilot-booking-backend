# 🚀 Pilot Booking System - Core API
**Enterprise Backend Engine**

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://www.java.com/) [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=flat-square&logo=spring&logoColor=white)](https://spring.io/projects/spring-boot) [![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14%2B-316192?style=flat-square&logo=postgresql&logoColor=white)](https://www.postgresql.org/) [![Redis](https://img.shields.io/badge/Redis-6.x-DC382D?style=flat-square&logo=redis&logoColor=white)](https://redis.io/) [![Flyway](https://img.shields.io/badge/Flyway-Migrations-CC0200?style=flat-square&logo=flyway&logoColor=white)](https://flywaydb.org/)

The authoritative, business-critical domain layer powering the Pilot Booking System. Designed with strict architectural discipline, this service orchestrates stateless authentication, dynamic booking workflows, analytics aggregation, and secure file management.

---

## 🏛️ Architectural Philosophy

Engineered for production scalability and operational transparency, the system enforces a strict separation of concerns: `Controller → Service → Repository → Database`.
* **Stateless & Horizontally Scalable:** Optimized for distributed deployments.
* **Domain Integrity:** Zero business logic in controllers; zero persistence logic in services.
* **Deterministic Evolution:** 100% version-controlled database schemas via Flyway.

---

## ✨ Core Functional Domains

1. **Identity & Access Control:** JWT-based stateless authentication with Role-Based Access Control (RBAC) and strict 15-minute token lifecycles.
2. **Booking Orchestration:** State-driven lifecycle handling *Soft Booking* (reservations) and *Hard Booking* (finalized confirmations) with concurrency conflict prevention.
3. **Storage Management:** Secure multipart upload pipelines for service catalogs and payment proofs, abstracted for future cloud-native migration (e.g., AWS S3).
4. **Metrics Analytics:** Performance-conscious aggregation of revenue, booking distributions, and active services.

---

## ⚙️ Getting Started

### Prerequisites
* Java 17+
* Maven
* PostgreSQL 14+
* Redis 6+ *(Optional, for optimized booking validation caching)*

### 1. Database Provisioning
```sql
CREATE DATABASE pilot_booking_db;