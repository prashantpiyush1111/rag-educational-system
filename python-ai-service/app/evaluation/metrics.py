def precision_at_k(results, relevant, k):
    if k <= 0:
        return 0.0
    top = list(results)[:k]
    return sum(1 for item in top if item in relevant) / k

def reciprocal_rank(results, relevant):
    for index, item in enumerate(results, start=1):
        if item in relevant:
            return 1.0 / index
    return 0.0
