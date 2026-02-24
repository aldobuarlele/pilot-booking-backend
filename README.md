# Pilot Booking System - Backend API

A robust, scalable, and enterprise-ready backend RESTful API for a dynamic booking engine. Built with Java Spring Boot, this system is designed to handle flexible booking scenarios including hotel reservations, car rentals, and travel packages. It features a state-driven architecture, a dynamic quota system, and seamless OTP-based authentication.

## Core Features

* **State-Driven Booking Engine**: Handles complex booking lifecycles (`SOFT_BOOKED`, `PENDING_APPROVAL`, `HARD_BOOKED`, `CANCELLED`).
* **Dynamic Quota & Availability**: Advanced date-overlapping prevention combined with a daily quota system. Prevents double-booking automatically.
* **OTP Authentication**: Passwordless login for clients using Redis-backed One-Time Passwords.
* **Dynamic Theming (White-label)**: System configuration endpoints to drive dynamic UI rendering on the frontend (e.g., primary colors, logos, and business names).
* **Multipart File Handling**: Secure local storage handling for payment proof uploads.
* **Role-Based Access Control (RBAC)**: JWT-secured endpoints separating `ADMIN` and `USER` privileges.
* **Automated Database Migrations**: Flyway integration for reliable schema versioning.
* **Global Exception Handling**: Standardized JSON error responses for seamless frontend integration.

## Tech Stack

* **Language**: Java 17
* **Framework**: Spring Boot 3.x
* **Database**: PostgreSQL
* **Caching & Session**: Redis
* **Security**: Spring Security & JWT (JSON Web Tokens)
* **ORM**: Spring Data JPA / Hibernate
* **Migration**: Flyway
* **Build Tool**: Maven

## Prerequisites

Ensure you have the following installed on your local machine:
* Java Development Kit (JDK) 17
* PostgreSQL (Running on port 5432)
* Redis (Running on port 6379)
* Maven

## Environment Setup

Duplicate the example environment file and configure your local credentials.

```bash
cp .env.example .env
