from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.api.routes import query, ingest, health

app = FastAPI(title="RAG Educational System - AI Service")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=False,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(ingest.router, prefix="/documents", tags=["Documents"])
app.include_router(query.router, prefix="/query", tags=["Query"])
app.include_router(health.router, tags=["Health"])