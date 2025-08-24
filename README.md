<h1>🛒 SpringMall</h1>
A cloud-native, fault-tolerant, and scalable e-commerce platform built using Spring Boot microservices. This project demonstrates best practices in distributed system design using Spring Cloud, JWT Security, Circuit Breakers, SAGA Pattern, and asynchronous messaging with RabbitMQ.

<h2>🚀 Features</h2>

✅ Microservices architecture using Spring Boot
✅ JWT-based authentication & authorization
✅ API Gateway for unified access and routing
✅ Eureka Server for service discovery
✅ Resilience4j circuit breaker for fault tolerance
✅ Saga Pattern for distributed transaction management
✅ REST APIs for synchronous communication
✅ RabbitMQ for asynchronous event-driven messaging
✅ Config Server for centralized configuration
✅ Docker-ready architecture

<h2>🧱 Microservices List</h2>
| Service                  | Description                                           |
| ------------------------ | ----------------------------------------------------- |
| **API Gateway**          | Entry point to the system; handles routing & security |
| **Auth Service**         | Handles login, registration, JWT token generation     |
| **User Service**         | Manages user profiles and roles                       |
| **Product Service**      | Catalog management: CRUD operations on products       |
| **Order Service**        | Manages orders, implements saga coordination          |
| **Payment Service**      | Handles payment processing, communicates via RabbitMQ |
| **Inventory Service**    | Tracks product stock levels and availability          |
| **Notification Service** | Sends emails or messages for order events             |
| **Config Server**        | Centralized configuration management                  |
| **Eureka Server**        | Service registry and discovery                        |

<h2>⚙️ Tech Stack</h2>
**🔧 Backend**

Java 17+

Spring Boot

Spring Cloud (Gateway, Eureka, Config)

Spring Security + JWT

Spring Web

Spring Data JPA

Resilience4j (Circuit Breaker)

RabbitMQ (AMQP Messaging)

Saga Pattern (via choreography or orchestrator pattern)

Lombok

</h2>🛢️ Databases</h2>

PostgreSQL 
