import pytest
from unittest.mock import patch, MagicMock

@patch("app.rag.core.retriever.get_collection")
@patch("app.rag.embeddings.encoder.embed_text")
def test_retrieve_relevant_chunks(mock_embed, mock_get_collection):
    mock_embed.return_value = [0.1, 0.2, 0.3]

    mock_collection = MagicMock()
    mock_collection.query.return_value = {
        "documents": [["This is a test chunk."]],
        "metadatas": [[{"source": "test.pdf"}]]
    }
    mock_get_collection.return_value = mock_collection

    from app.rag.core.retriever import retrieve_relevant_chunks
    result = retrieve_relevant_chunks("What is testing?", top_k=1)

    assert len(result) == 1
    assert result[0]["text"] == "This is a test chunk."
    assert result[0]["source"] == "test.pdf"