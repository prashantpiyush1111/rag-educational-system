import hashlib
import json
from functools import lru_cache

_cache_store = {}

def _make_cache_key(question: str, top_k: int) -> str:
    raw = f"{question}:{top_k}"
    return hashlib.md5(raw.encode()).hexdigest()

def get_cached_response(question: str, top_k: int):
    key = _make_cache_key(question, top_k)
    return _cache_store.get(key)

def set_cached_response(question: str, top_k: int, response: dict):
    key = _make_cache_key(question, top_k)
    _cache_store[key] = response