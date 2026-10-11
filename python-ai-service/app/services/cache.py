import hashlib
import json
import logging
from threading import RLock
from app.config.settings import settings

log = logging.getLogger(__name__)
_memory: dict[str, dict] = {}
_lock = RLock()
_redis = None

def _client():
    global _redis
    if _redis is None:
        try:
            import redis
            client = redis.Redis(host=settings.REDIS_HOST, port=settings.REDIS_PORT, socket_connect_timeout=1, socket_timeout=1, decode_responses=True)
            client.ping()
            _redis = client
        except Exception as exc:
            log.warning("Redis cache unavailable; using in-memory fallback: %s", exc)
            _redis = False
    return _redis

def _make_cache_key(question: str, top_k: int) -> str:
    raw = f"{question.strip().lower()}:{top_k}"
    return "rag:query:" + hashlib.sha256(raw.encode("utf-8")).hexdigest()

def get_cached_response(question: str, top_k: int):
    key = _make_cache_key(question, top_k)
    client = _client()
    if client:
        try:
            value = client.get(key)
            return json.loads(value) if value else None
        except Exception as exc:
            log.warning("Redis cache read failed: %s", exc)
    with _lock:
        return _memory.get(key)

def set_cached_response(question: str, top_k: int, response: dict):
    key = _make_cache_key(question, top_k)
    client = _client()
    if client:
        try:
            client.setex(key, settings.REDIS_CACHE_TTL_SECONDS, json.dumps(response))
            return
        except Exception as exc:
            log.warning("Redis cache write failed: %s", exc)
    with _lock:
        _memory[key] = response
