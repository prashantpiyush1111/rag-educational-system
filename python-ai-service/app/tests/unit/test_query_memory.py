import asyncio
from unittest.mock import patch

from app.api.routes import query as query_route
from app.models.request import QueryRequest


def test_query_scopes_conversation_history_to_user_and_session():
    request = QueryRequest(question="What is RAG?", session_id="shared-session")
    chunks = [{"source": "guide.pdf", "text": "RAG combines retrieval and generation."}]

    with (
        patch.object(query_route, "get_cached_response", return_value=None),
        patch.object(query_route, "get_history", return_value=[]) as get_history,
        patch.object(query_route, "retrieve_relevant_chunks", return_value=chunks),
        patch.object(query_route, "rerank_chunks", return_value=chunks),
        patch.object(query_route, "generate_answer", return_value="A grounded answer"),
        patch.object(query_route, "set_cached_response"),
        patch.object(query_route, "append_turn") as append_turn,
    ):
        response = asyncio.run(
            query_route.query_documents(request, x_user_id="user-42")
        )

    assert response.answer == "A grounded answer"
    get_history.assert_called_once_with("user-42:shared-session")
    append_turn.assert_called_once_with(
        "user-42:shared-session", "What is RAG?", "A grounded answer"
    )
