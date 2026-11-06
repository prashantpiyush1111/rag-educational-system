from fastapi.testclient import TestClient
from app.main import app
from app.config.settings import settings

client = TestClient(app)

def test_health_endpoint():
    response = client.get("/health")
    assert response.status_code == 200
    assert response.json()["status"] == "ok"

def test_query_rejects_missing_key():
    response = client.post("/query/", json={"question":"What is RAG?"})
    assert response.status_code == 401

def test_documents_requires_user_id():
    response = client.get("/documents/", headers={"X-API-Key": settings.INTERNAL_API_KEY})
    assert response.status_code == 400
