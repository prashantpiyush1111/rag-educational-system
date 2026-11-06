from app.evaluation.metrics import precision_at_k, reciprocal_rank

def evaluate_retrieval(results: list[str], relevant: set[str], k: int = 5) -> dict[str, float]:
    return {"precision_at_k": precision_at_k(results, relevant, k), "reciprocal_rank": reciprocal_rank(results, relevant)}
