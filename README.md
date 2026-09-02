# 🎓 RAG Educational System

> A retrieval-augmented educational platform combining application services, vector search, caching, and event-driven infrastructure to support AI-assisted learning experiences.

## 📌 Overview

**RAG Educational System** is a multi-service project centered on **Retrieval-Augmented Generation (RAG)** for educational use cases.

The repository brings together application components with supporting infrastructure for relational data, caching, vector retrieval, and event-driven processing. Environment-specific secrets and local service data are intentionally excluded from version control.

## 🧩 Core Architecture

```text
                    ┌──────────────────────┐
                    │ Educational Client   │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │ Application Services │
                    └───────┬──────┬───────┘
                            │      │
                ┌───────────┘      └────────────┐
                ▼                               ▼
        PostgreSQL 16                         Redis 7
                │                               │
                └──────────────┬────────────────┘
                               ▼
                         Qdrant Vector DB
                               │
                               ▼
                     Retrieval / RAG Layer
                               │
                               ▼
                         AI-powered answers

              Kafka provides event-driven messaging
```

## 🛠️ Technology Stack

| Area | Technologies |
|---|---|
| Application | Java / Python services in the repository |
| Database | PostgreSQL 16 |
| Cache | Redis 7 |
| Vector Database | Qdrant |
| Messaging | Apache Kafka / Confluent Kafka |
| Frontend | Project frontend module |
| AI Architecture | Retrieval-Augmented Generation (RAG) |
| Infrastructure | Docker Compose |

## 🧠 RAG Workflow

```text
User Question
     ↓
Retrieve Relevant Knowledge
     ↓
Vector Search
     ↓
Build Context
     ↓
Generate Context-Aware Response
     ↓
Return Educational Answer
```

The vector database is used for semantic retrieval, while PostgreSQL provides relational persistence and Redis supports fast-access application state/cache use cases.

## 🏗️ Local Infrastructure

The provided Docker Compose configuration defines:

- **PostgreSQL 16** on port `5432`
- **Redis 7** on port `6379`
- **Qdrant** on ports `6333` and `6334`
- **Kafka** on ports `9092` and `29092`

Persistent Docker volumes are configured for PostgreSQL, Redis, and Qdrant data.

## 🚀 Getting Started

### Prerequisites

- Git
- Docker and Docker Compose
- The runtime/tooling required by the individual application modules

### 1. Clone

```bash
git clone https://github.com/prashantpiyush1111/rag-educational-system.git
cd rag-educational-system
```

### 2. Configure Environment

The infrastructure uses environment variables such as `DB_PASSWORD`. Create the required local environment configuration without committing secrets.

### 3. Start Infrastructure

```bash
docker compose up -d
```

### 4. Verify Services

Check the containers with:

```bash
docker compose ps
```

Application-specific run commands may vary by module and should be executed from the relevant service directory.

## 🔐 Security & Repository Hygiene

The repository excludes local environment files, generated vector-database data, Python virtual environments, build artifacts, and infrastructure secrets from version control.

> Never commit database passwords, API keys, tokens, or production environment files.

## 📂 Repository Organization

The repository includes separate areas for application services, a frontend, infrastructure configuration, and local development dependencies. See the source tree for the current module layout.

## 🎯 Project Goals

- Explore practical RAG architecture for educational applications
- Combine semantic retrieval with application/business data
- Use containerized infrastructure for reproducible local development
- Practice integration across databases, caches, vector stores, and messaging systems

## 🗺️ Future Direction

The architecture can be extended with richer educational workflows, stronger retrieval/evaluation pipelines, production authentication, observability, and deployment automation as the project evolves.

## 👨‍💻 Author

**Prashant Maurya**  
GitHub: [@prashantpiyush1111](https://github.com/prashantpiyush1111)

## 📄 License

See the repository license for current usage terms.
