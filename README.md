# 🛒 SpringMall

A cloud-native, fault-tolerant, and scalable **e-commerce platform** built using **Spring Boot microservices**. This project demonstrates best practices in distributed system design using **Spring Cloud**, **JWT Security**, **Circuit Breakers**, **SAGA Pattern**, and **asynchronous messaging** with **RabbitMQ**.

---

## 🚀 Features

- ✅ Microservices architecture using Spring Boot
- ✅ JWT-based authentication & authorization
- ✅ API Gateway for unified access and routing
- ✅ Eureka Server for service discovery
- ✅ Resilience4j circuit breaker for fault tolerance
- ✅ Saga Pattern for distributed transaction management
- ✅ REST APIs for synchronous communication
- ✅ RabbitMQ for asynchronous event-driven messaging
- ✅ Config Server for centralized configuration
- ✅ Docker-ready architecture (optional)

---

## 🧱 Microservices List

| Microservice         | Description                                              |
|----------------------|----------------------------------------------------------|
| **API Gateway**      | Entry point to the system; handles routing & security    |
| **Auth Service**     | Manages user login, registration, JWT token generation   |
| **User Service**     | Handles user profiles and roles                          |
| **Product Service**  | Manages product catalog and CRUD operations              |
| **Order Service**    | Places and tracks orders; implements Saga coordination   |
| **Payment Service**  | Simulates payment processing                             |
| **Notification Service** | Sends emails or messages on order events             |
| **Config Server**    | Centralized config management for all services           |
| **Eureka Server**    | Service registry and discovery                           |

---

## ⚙️ Tech Stack

### 🔧 Backend
- Java 17+
- Spring Boot
- Spring Cloud (Gateway, Eureka, Config)
- Spring Security + JWT
- Spring Data JPA
- Resilience4j (Circuit Breaker)
- RabbitMQ
- Saga Pattern (via choreography or orchestration)
- Lombok

### 🛢️ Databases
- PostgreSQL

### 🐳 DevOps / Deployment
- Docker
- Docker Compose

---


---

## 🔐 Security

- Stateless authentication using **JWT**
- Role-based access control (RBAC)
- Token validation at API Gateway level
- Secure endpoints using Spring Security

---

## 🔁 Communication Patterns

- 🔄 **Synchronous**: REST APIs between services  
  _(e.g., Order Service calling Product)_

- 📩 **Asynchronous**: RabbitMQ used for events like:
  - Sign up user → send notification to user via java mail
  - Payment success → Inventory update

---

## 🔄 Distributed Transactions

Implemented using the **Saga Pattern** to manage data consistency across distributed services using domain events and message brokers.

---

## 🏗️ How to Run Locally

### 1. Clone the Repository
```bash
git clone https://github.com/MohdAqib8267/SpringMall
cd SpringMall

### 2. start Infrastructure

docker-compose up -d

### 3. Run Services Individually

Use your IDE or terminal to run each microservice locally.
Each service will register with Eureka and load config from the Config Server.



