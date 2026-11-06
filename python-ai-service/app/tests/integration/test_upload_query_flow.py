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


from unittest.mock import patch


def test_document_upload_then_query_smoke():
    chunks = [{
        "id": "biology.pdf:0",
        "source": "biology.pdf",
        "text": "Photosynthesis converts light energy into chemical energy.",
        "embedding": [0.1, 0.2, 0.3],
    }]
    headers = {"X-API-Key": settings.INTERNAL_API_KEY, "X-User-Id": "student-7"}

    with (
        patch("app.api.routes.ingest.load_document", return_value="Photosynthesis converts light energy into chemical energy."),
        patch("app.api.routes.ingest.embed_chunks", return_value=chunks),
        patch("app.api.routes.ingest.store_embeddings") as store,
        patch("app.api.routes.query.get_cached_response", return_value=None),
        patch("app.api.routes.query.get_history", return_value=[]),
        patch("app.api.routes.query.retrieve_relevant_chunks", return_value=chunks),
        patch("app.api.routes.query.rerank_chunks", return_value=chunks),
        patch("app.api.routes.query.generate_answer", return_value="Photosynthesis converts light into chemical energy."),
        patch("app.api.routes.query.set_cached_response"),
        patch("app.api.routes.query.append_turn"),
    ):
        upload = client.post(
            "/documents/upload",
            headers=headers,
            files={"file": ("biology.pdf", b"test pdf bytes", "application/pdf")},
        )
        assert upload.status_code == 200
        assert upload.json()["chunks_created"] == 1
        store.assert_called_once()

        query = client.post(
            "/query/",
            headers=headers,
            json={"question": "What is photosynthesis?", "top_k": 4, "session_id": "smoke-session"},
        )

    assert query.status_code == 200
    assert "chemical energy" in query.json()["answer"]
