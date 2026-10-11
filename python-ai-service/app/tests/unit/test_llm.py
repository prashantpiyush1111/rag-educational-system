from unittest.mock import MagicMock, patch
from app.rag.llm.generator import generate_answer

@patch("app.rag.llm.generator.get_groq_client")
def test_generate_answer_uses_prompt_and_context(mock_client):
    completion = MagicMock()
    completion.choices = [MagicMock(message=MagicMock(content="Grounded answer"))]
    mock_client.return_value.chat.completions.create.return_value = completion
    answer = generate_answer("What is it?", [{"source":"notes.pdf","text":"It is a test."}], "user: hi")
    assert answer == "Grounded answer"
    kwargs = mock_client.return_value.chat.completions.create.call_args.kwargs
    assert "notes.pdf" in kwargs["messages"][1]["content"]
    assert "user: hi" in kwargs["messages"][1]["content"]
