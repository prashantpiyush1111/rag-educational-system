from fastapi.testclient import TestClient
from app.main import app

def test_metrics_endpoint_is_prometheus_format():
    response = TestClient(app).get("/metrics")
    assert response.status_code == 200
    assert "rag_http_requests_total" in response.text or "rag_http_request_duration_seconds" in response.text
