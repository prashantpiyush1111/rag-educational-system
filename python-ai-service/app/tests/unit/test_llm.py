import pytest
from unittest.mock import patch, MagicMock

@patch("app.rag.llm.client.get_openai_client")
def test_generate_answer(mock_get_client):
    mock_client = MagicMock()
    mock_response = MagicMock()
    mock_response.choices[0].message.content = "This is a test answer."
    mock_client.chat.completions.create.return_value = mock_response
    mock_get_client.return_value = mock_client

    from app.rag.llm.generator import generate_answer
    context_chunks = [{"text": "Sample context.", "source": "test.pdf"}]
    result = generate_answer("What is this?", context_chunks)

    assert result == "This is a test answer."
    mock_client.chat.completions.create.assert_called_once()