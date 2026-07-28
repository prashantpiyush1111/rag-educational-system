import pytest
from unittest.mock import patch
from fastapi.testclient import TestClient
from app.main import app

client = TestClient(app)

def test_health_endpoint():
    response = client.get("/health")
    assert response.status_code == 200
    assert response.json()["status"] == "healthy"

@patch("app.api.routes.query.retrieve_relevant_chunks")
@patch("app.api.routes.query.generate_answer")
def test_query_endpoint(mock_generate, mock_retrieve):
    mock_retrieve.return_value = [{"text": "Sample context", "source": "test.pdf"}]
    mock_generate.return_value = "This is the answer."

    response = client.post("/query/", json={"question": "What is RAG?", "top_k": 4})

    assert response.status_code == 200
    data = response.json()
    assert data["answer"] == "This is the answer."
    assert data["sources"] == ["test.pdf"]