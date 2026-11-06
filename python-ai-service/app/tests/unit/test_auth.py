from fastapi.testclient import TestClient
from app.main import app
from app.config.settings import settings

def test_query_requires_api_key():
    response = TestClient(app).post("/query/", json={"question":"hello"})
    assert response.status_code == 401

def test_health_is_public():
    response = TestClient(app).get("/health")
    assert response.status_code == 200

def test_authenticated_query_requires_user_header():
    response = TestClient(app).post("/query/", headers={"X-API-Key":settings.INTERNAL_API_KEY}, json={"question":"hello"})
    assert response.status_code == 400
    assert response.json()["detail"] == "Missing X-User-Id"
