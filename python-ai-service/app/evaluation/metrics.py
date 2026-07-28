def calculate_precision_at_k(retrieved_sources: list[str], relevant_sources: list[str], k: int) -> float:
    top_k = retrieved_sources[:k]
    if not top_k:
        return 0.0
    relevant_retrieved = sum(1 for source in top_k if source in relevant_sources)
    return relevant_retrieved / len(top_k)

def calculate_recall_at_k(retrieved_sources: list[str], relevant_sources: list[str], k: int) -> float:
    if not relevant_sources:
        return 0.0
    top_k = retrieved_sources[:k]
    relevant_retrieved = sum(1 for source in top_k if source in relevant_sources)
    return relevant_retrieved / len(relevant_sources)

def calculate_mrr(retrieved_sources: list[str], relevant_sources: list[str]) -> float:
    for i, source in enumerate(retrieved_sources):
        if source in relevant_sources:
            return 1.0 / (i + 1)
    return 0.0

def calculate_f1_score(precision: float, recall: float) -> float:
    if precision + recall == 0:
        return 0.0
    return 2 * (precision * recall) / (precision + recall)