from prometheus_client import Counter, Histogram, generate_latest, CONTENT_TYPE_LATEST
from fastapi import APIRouter, Response

REQUEST_COUNT = Counter("rag_http_requests_total", "HTTP requests handled", ["method", "path", "status"])
REQUEST_LATENCY_SECONDS = Histogram("rag_http_request_duration_seconds", "HTTP request latency")

router = APIRouter()
@router.get("/metrics", include_in_schema=False)
def metrics():
    return Response(generate_latest(), media_type=CONTENT_TYPE_LATEST)
