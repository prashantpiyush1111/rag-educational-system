from app.evaluation.metrics import precision_at_k, reciprocal_rank

def test_precision_at_k():
    assert precision_at_k(["a","b","c"], {"a","c"}, 2) == 0.5

def test_reciprocal_rank():
    assert reciprocal_rank(["x","b","a"], {"a","b"}) == 0.5
