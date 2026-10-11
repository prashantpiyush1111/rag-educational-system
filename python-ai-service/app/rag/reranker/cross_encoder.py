from functools import lru_cache
from app.config.settings import settings

@lru_cache(maxsize=1)
def _model():
    from sentence_transformers import CrossEncoder
    return CrossEncoder(settings.RERANKER_MODEL)

def rerank_chunks(question: str, chunks: list[dict], top_k: int) -> list[dict]:
    if not settings.RERANKER_ENABLED or len(chunks) < 2:
        return chunks[:top_k]
    model = _model()
    pairs = [(question, chunk.get("text", "")) for chunk in chunks]
    scores = model.predict(pairs)
    ranked = sorted(zip(chunks, scores), key=lambda item: float(item[1]), reverse=True)
    return [{**chunk, "rerank_score": float(score)} for chunk, score in ranked[:top_k]]
