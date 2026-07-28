from app.evaluation.metrics import (
    calculate_precision_at_k,
    calculate_recall_at_k,
    calculate_mrr,
    calculate_f1_score
)
from app.rag.core.retriever import retrieve_relevant_chunks

def evaluate_retrieval(test_queries: list[dict], k: int = 4) -> dict:
    results = []

    for test_case in test_queries:
        question = test_case["question"]
        relevant_sources = test_case["relevant_sources"]

        retrieved_chunks = retrieve_relevant_chunks(query=question, top_k=k)
        retrieved_sources = [chunk["source"] for chunk in retrieved_chunks]

        precision = calculate_precision_at_k(retrieved_sources, relevant_sources, k)
        recall = calculate_recall_at_k(retrieved_sources, relevant_sources, k)
        mrr = calculate_mrr(retrieved_sources, relevant_sources)
        f1 = calculate_f1_score(precision, recall)

        results.append({
            "question": question,
            "precision": precision,
            "recall": recall,
            "mrr": mrr,
            "f1_score": f1
        })

    avg_precision = sum(r["precision"] for r in results) / len(results)
    avg_recall = sum(r["recall"] for r in results) / len(results)
    avg_mrr = sum(r["mrr"] for r in results) / len(results)
    avg_f1 = sum(r["f1_score"] for r in results) / len(results)

    return {
        "individual_results": results,
        "average_precision": avg_precision,
        "average_recall": avg_recall,
        "average_mrr": avg_mrr,
        "average_f1_score": avg_f1
    }