from app.services.cache import _make_cache_key

def test_cache_key_normalizes_question():
    assert _make_cache_key("  Explain Cells ",4) == _make_cache_key("explain cells",4)

def test_cache_key_includes_top_k():
    assert _make_cache_key("cells",4) != _make_cache_key("cells",5)
