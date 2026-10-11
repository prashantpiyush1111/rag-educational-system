import time
from fastapi import FastAPI, Request
from fastapi.middleware.cors import CORSMiddleware
from app.api.routes import query, ingest, health
from app.api.metrics import router as metrics_router, REQUEST_COUNT, REQUEST_LATENCY_SECONDS
from app.config.settings import settings

app = FastAPI(title="RAG Educational System - AI Service")
app.add_middleware(CORSMiddleware, allow_origins=settings.cors_origins, allow_credentials=False, allow_methods=["GET", "POST", "DELETE", "OPTIONS"], allow_headers=["Authorization", "Content-Type", "X-API-Key", "X-User-Id"])
@app.middleware("http")
async def observe_requests(request: Request, call_next):
    start = time.perf_counter()
    status = 500
    try:
        response = await call_next(request)
        status = response.status_code
        return response
    finally:
        REQUEST_COUNT.labels(request.method, request.url.path, str(status)).inc()
        # Histogram covers end-to-end request duration; nested query timing is harmless.
        REQUEST_LATENCY_SECONDS.observe(time.perf_counter() - start)

app.include_router(ingest.router, prefix="/documents", tags=["Documents"])
app.include_router(query.router, prefix="/query", tags=["Query"])
app.include_router(health.router, tags=["Health"])
app.include_router(metrics_router, tags=["Metrics"])
