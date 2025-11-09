# 🎓 Student Registration System

A **fast**, **scalable**, and **maintainable** backend system built with **Spring Boot**, designed to handle student registration workflows with robust document generation, real-time updates, and event-driven processing.

---

## 🚀 Overview

The **Student Registration System** is built with modern backend engineering practices that emphasize performance, clean architecture, and ease of collaboration. It combines **Event-Driven Architecture (EDA)** and **Layered Architecture**, following the **MVC pattern** and **Domain-Driven Design (DDD)** principles.

It supports **real-time dashboards** using **Spring Server-Sent Events (SSE)** and ensures smooth **frontend integration** with **OpenAPI documentation** and **GitHub CI/CD pipelines**.

---

## 🏗️ Architecture Overview

### 🔹 Key Design Principles

- **Domain-Driven Design (DDD)** for clear business logic boundaries.
- **Event-Driven Architecture** for decoupled communication and scalability.
- **Layered Architecture (MVC pattern)** for maintainable structure.
- **Factory Design Pattern** to manage object creation.
- **Partitioning Strategy**
    - **Technical Partition:** utility and infrastructure layers.
    - **Domain Partition:** business-specific modules.

### 🔹 Major Features

- 📄 **Document Generation** (with concurrent processing via multithreading)
- 📧 **Asynchronous Email Sending**
- 🔄 **Real-Time Dashboard Updates** using **Spring SSE**
- 🧩 **Event Publishing & Listening** for domain-driven event flow
- 🧠 **Custom Security Layer** (JWT, Token Validation, etc.)
- 🐳 **Containerized Deployment** using **Docker Compose**
- 🔁 **Automated CI/CD** with **GitHub Actions**
- 🧾 **Interactive API Docs** powered by **OpenAPI (Swagger)**

---

## 🧱 Folder Structure Overview

```
student-registration-system/
│
├── config/
│   ├── annotation/
│   ├── beans/
│   ├── event/
│   ├── exception/
│   ├── listener/
│   ├── request/
│   ├── response/
│   ├── service/
│   ├── utils/
│   └── validators/
│
├── core/
│   └── nrc/
│       ├── model/
│       └── service/
│
├── data/
│   ├── docsUtils/
│   ├── email/
│   ├── enums/
│   ├── models/
│   ├── redis/
│   ├── repositories/
│   └── storage/
│
├── features/
│   ├── feature-name/
│   │   ├── controller/
│   │   ├── service/
│   │   └── impl/
│
├── security/
│   ├── config/
│   ├── filter/
|   ├── ............
│
├── sse/
│   ├── config/
│   └── ............
│
├── startup/
│   └── (data-seeding)
│
└── docker-compose.yml

```

---

## ⚙️ Tech Stack

| Category | Technology |
| --- | --- |
| **Backend Framework** | Spring Boot 3.5 |
| **Language** | Java 21 |
| **Database** | MySQL / H2 |
| **Cache** | Redis |
| **Messaging / Event Handling** | Spring Application Events |
| **Real-Time Communication** | Spring Server-Sent Events (SSE) |
| **Build Tool** | Maven / Gradle |
| **API Documentation** | OpenAPI (Swagger UI) |
| **Containerization** | Docker, Docker Compose |
| **CI/CD** | GitHub Actions |
| **Version Control** | Git (Modified Git Flow Strategy) |

---

## 🧩 Branching Strategy (Modified Git Flow)

- **`main`** → Production-ready code
- **`develop`** → Integration branch for completed features and automatic deployment branch
- **`dev-hlyam*`** → Individual developer name

This approach ensures code stability while enabling rapid feature delivery.

---

## 🔄 Event-Driven + Layered Architecture

- **Events:** Core domain and system events are published via Spring’s event publisher.
- **Listeners:** Subscribed services react asynchronously, enabling decoupling.
- **Layering:**
    - **Controller Layer** → Handles API requests.
    - **Service Layer** → Encapsulates business logic.
    - **Repository Layer** → Manages data persistence.

---

## ⚡ Concurrency and Multithreading

- **Document Generation & Email Sending** handled via **multithreaded executors** for faster response times.
- **Thread Pool Management** ensures optimal resource usage and thread safety.

---

## 📡 Real-Time Dashboard with SSE

- **Server-Sent Events (SSE)** streams updates to connected clients in real-time.
- **Topic Management** allows grouping and broadcasting of live events to specific dashboards.

---

## 🔐 Security

- Custom **Spring Security configuration**.
- **JWT token validation** and role-based access control (RBAC).
- Token filters for request interception and authentication enforcement.

---

## 🧰 Integration & Deployment

- **OpenAPI Spec** for seamless frontend collaboration.
- **Docker Compose** to orchestrate multi-service deployment (App + DB + Redis).
- **GitHub Actions CI/CD** pipeline for automated testing, build, and deployment.

---

## 🧾 Setup Instructions

### 1️⃣ Clone Repository

```bash
git clone https://github.com/HlyamHtetKyaw/student-registration-java.git
cd student-registration-system
```

### 2️⃣ Configure Environment

Update your `.env` file.

### 3️⃣ Build & Run with Docker

```bash
docker-compose up --build (Note: this will pull my latest image from docker hub)
```

### 4️⃣ Access Services

- **API Docs:** `http://localhost:8080/swagger-ui/index.html`
- **Real-Time Dashboard SSE:** `http://localhost:8080/tutgi/api/v1/dean/subscribe`

---

## 🧪 Testing

- Unit and integration tests with **JUnit 5** and **Mockito**.

---

## 📬 Contact

For questions or contributions, feel free to reach out:

**Maintainer:** Hlyam Htet Kyaw

**Email:** hlyamhtet.dev@gmail.com

**GitHub:** [github.com/HlyamHtetKyaw]

---
