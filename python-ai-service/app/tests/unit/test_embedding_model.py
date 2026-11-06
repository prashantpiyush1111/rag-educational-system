from unittest.mock import patch

from app.rag.embeddings import model


def test_get_embedding_model_uses_configured_default():
    model._model = None
    with patch("app.rag.embeddings.model.SentenceTransformer") as sentence_transformer:
        instance = sentence_transformer.return_value

        result = model.get_embedding_model()

        sentence_transformer.assert_called_once_with(
            "sentence-transformers/all-MiniLM-L6-v2"
        )
        assert result is instance

    model._model = None
