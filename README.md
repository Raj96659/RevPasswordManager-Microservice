# Password Manager Microservices Application

## Project Overview

The Password Manager is a cloud-native microservices web application designed to securely store and manage credentials for multiple online accounts.

This project modernizes a monolithic Spring Boot application into a scalable microservices architecture using Spring Cloud technologies.

Users can securely generate passwords, store encrypted credentials, manage vault entries, and perform security audits through an intuitive Angular web interface.

---

## Architecture

Angular Frontend → API Gateway → Microservices

Microservices:
- User Service
- Vault Service
- Generator Service
- Security Service
- Notification Service

Infrastructure:
- Eureka Server (Service Discovery)
- Config Server (Centralized Configuration)
- API Gateway (Routing & Security)

---

## Microservices

### User Service
Handles authentication and account management.

Features:
- User registration
- Login
- Two-factor authentication
- Security questions
- Password recovery

### Vault Service
Manages encrypted credential storage.

Features:
- Add / update / delete credentials
- Search and filter entries
- Favorite passwords
- Export / import encrypted backups

### Generator Service
Generates strong customizable passwords.

Features:
- Custom length
- Character sets
- Strength indicator

### Security Service
Performs security analysis.

Features:
- Weak password detection
- Password reuse detection
- Security audit reports

### Notification Service
Sends security alerts and notifications.

---

## Security Features

- BCrypt password hashing
- AES encrypted vault passwords
- PBKDF2 key derivation
- JWT authentication
- Two-factor authentication
- Security questions
- Password strength analysis

---

## Technology Stack

Backend:
- Java 17
- Spring Boot
- Spring Cloud
- OpenFeign
- Resilience4j
- MySQL

Frontend:
- Angular
- TypeScript
- Bootstrap / Angular Material

Infrastructure:
- Eureka Server
- Config Server
- API Gateway

---

## Core Features

- Secure password vault
- Password generator
- Search / filter vault entries
- Favorite passwords
- Security audit reports
- Import / export encrypted backups
- Two-factor authentication

---

## Future Improvements

- Docker containerization
- Docker Compose orchestration
- CI/CD pipeline
- AWS cloud deployment

---

## Author

Raj – Password Manager Microservices Project
