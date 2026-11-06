from app.services import cache

def test_cache_key_normalizes_question():
    assert cache._make_cache_key("  Explain Cells ",4) == cache._make_cache_key("explain cells",4)

def test_cache_key_includes_top_k():
    assert cache._make_cache_key("cells",4) != cache._make_cache_key("cells",5)

def test_cache_key_is_scoped_to_user():
    assert cache._make_cache_key("cells",4,"user-1") != cache._make_cache_key("cells",4,"user-2")

def test_invalidate_user_cache_only_removes_that_users_entries(monkeypatch):
    monkeypatch.setattr(cache, "_redis", False)
    cache._memory.clear()
    cache.set_cached_response("cells",4,{"answer":"old answer"},"user-1")
    cache.set_cached_response("cells",4,{"answer":"other user answer"},"user-2")

    cache.invalidate_user_cache("user-1")

    assert cache.get_cached_response("cells",4,"user-1") is None
    assert cache.get_cached_response("cells",4,"user-2") == {"answer":"other user answer"}
