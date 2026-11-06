# RAG Educational System

A multi-service educational assistant that answers questions grounded in user-uploaded documents. The browser talks only to the Java gateway; Java owns JWT authentication, user/document metadata, query logs and feedback, and forwards RAG work to the private Python service. ChromaDB remains the vector store.

## Architecture

```text
Browser / React + Vite
        |
        v
Nginx (port 8080; /api -> Java)
        |
        v
Java Spring Boot 3 / JWT / JPA / Redis (port 8080 inside compose)
  |             |                 |
  v             v                 v
PostgreSQL     Redis          Python FastAPI (private, port 8000)
rag_system                    X-API-Key + X-User-Id
                                  |
                                  v
                           ChromaDB persistent volume
                           Groq LLM + embeddings
```

PostgreSQL is the system of record for users, documents, query logs and feedback. Chroma metadata includes `user_id` and retrieval is restricted to that user. Python is not published to the host in the root Compose setup.

## Stack

- Frontend: React 18, Vite, React Router, Axios, Vitest and React Testing Library
- Gateway/API: Java 17, Spring Boot 3, Spring Security, JWT, JPA, PostgreSQL, Redis, Micrometer Prometheus
- RAG: Python 3.11, FastAPI, ChromaDB, sentence-transformers, Groq, Redis, Prometheus client
- Deployment: Docker Compose and Nginx

## Environment

Copy the example, then edit local values:

```bash
cp .env.example .env
```

Required for Docker Compose: `DB_PASSWORD`, `JWT_SECRET` (at least 32 random bytes), `GROQ_API_KEY`, and `INTERNAL_API_KEY`. Optional settings include `GROQ_MODEL`, `CORS_ORIGINS`, `MAX_FILE_SIZE`, and `REDIS_HOST`. Never commit `.env`, credentials, API tokens or production secrets.

## Run with Docker

From the repository root:

```bash
cp .env.example .env
# Edit .env and set real local values for DB_PASSWORD, JWT_SECRET,
# GROQ_API_KEY, and INTERNAL_API_KEY before starting.
docker compose config
docker compose up --build -d
docker compose ps
```

Open http://localhost:8080. The root Compose file publishes only Nginx, PostgreSQL and Redis. Java and Python are internal Compose services. On first startup, PostgreSQL initializes the `rag_system` database and creates the tables from `database/postgresql/init.sql`. If you already have an old local postgres volume, initialization scripts will not rerun automatically.

## Run services separately

Start PostgreSQL and Redis, set environment variables for the Java backend and Python AI service, then run:

```bash
cd python-ai-service
python -m venv .venv
# Activate .venv, then:
pip install -r requirements.txt
uvicorn app.main:app --reload --port 8000
```

In another terminal:

```bash
cd java-backend
mvn spring-boot:run
```

Then:

```bash
cd frontend
npm ci
npm run dev
```

For local Vite development, set `VITE_API_BASE` to the Java backend base URL plus `/api` (for example, `http://localhost:8080/api`). Never put the internal Python API key into a Vite variable.

## API summary

All browser API routes are served through Nginx at `/api`. Private Python service endpoints require `X-API-Key`; Java adds `X-User-Id` for user-isolated operations.

| Method | Route | Authentication | Purpose |
|---|---|---|---|
| POST | `/api/auth/register` | Public | Register account using `fullName`, `email`, `password` |
| POST | `/api/auth/login` | Public | Login and receive JWT |
| GET | `/api/auth/me` | JWT | Current account profile |
| POST | `/api/query` | JWT | Ask a document-grounded question |
| POST | `/api/documents/upload` | JWT | Upload PDF, TXT or DOCX |
| GET | `/api/documents` | JWT | List current user's documents |
| DELETE | `/api/documents/{id}` | JWT | Delete current user's document |
| GET | `/api/metrics` | JWT | User-level document/query metrics |
| POST | `/api/feedback` | JWT | Rate a query from 1 to 5 |
| GET | `/actuator/health` | Public | Java health check |
| GET | `/actuator/prometheus` | Public | Java Prometheus metrics |

## Tests

```bash
# Java
cd java-backend
mvn -B verify

# Python
cd ../python-ai-service
python -m pip install -r requirements.txt
pytest -q

# Frontend
cd ../frontend
npm ci
npm run build
npm test -- --run
```

The GitHub Actions CI workflow runs those jobs and Docker build checks on pushes/PRs.

## Project structure

- `java-backend/`: JWT gateway, validation, JPA entities, request/response DTOs and controllers
- `python-ai-service/`: FastAPI endpoints, document ingestion, Chroma retrieval, prompts, conversation memory, reranking and metrics
- `frontend/`: React pages/components and one Axios client
- `database/postgresql/init.sql`: PostgreSQL schema
- `docker-compose.yml`: canonical stack
- `frontend/nginx.conf`: static SPA hosting and API reverse proxy
