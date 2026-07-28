from sentence_transformers import SentenceTransformer
from app.config.settings import settings

_model = None

def get_embedding_model() -> SentenceTransformer:
    global _model
    if _model is None:
        _model = SentenceTransformer(settings.EMBEDDING_MODEL_NAME)
    return _model