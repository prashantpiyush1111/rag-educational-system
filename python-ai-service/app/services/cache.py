import hashlib,json,logging
from threading import RLock
from app.config.settings import settings

log=logging.getLogger(__name__)
_memory={}
_lock=RLock()
_redis=None

def _client():
 global _redis
 if _redis is None:
  try:
   import redis
   client=redis.Redis(host=settings.REDIS_HOST,port=settings.REDIS_PORT,socket_connect_timeout=1,socket_timeout=1,decode_responses=True)
   client.ping()
   _redis=client
  except Exception as exc:
   log.warning("Redis cache unavailable; using in-memory fallback: %s",exc)
   _redis=False
 return _redis

def _user_prefix(user_id:str|int|None=None)->str:
 return f"rag:query:{str(user_id or 'anonymous')}:"

def _make_cache_key(question:str,top_k:int,user_id:str|int|None=None)->str:
 raw=f"{question.strip().lower()}:{top_k}"
 return _user_prefix(user_id)+hashlib.sha256(raw.encode("utf-8")).hexdigest()

def get_cached_response(question:str,top_k:int,user_id:str|int|None=None):
 key=_make_cache_key(question,top_k,user_id)
 client=_client()
 if client:
  try:
   value=client.get(key)
   return json.loads(value) if value else None
  except Exception as exc:
   log.warning("Redis cache read failed: %s",exc)
   global _redis
   _redis=False
 with _lock:
  return _memory.get(key)

def set_cached_response(question:str,top_k:int,response:dict,user_id:str|int|None=None):
 key=_make_cache_key(question,top_k,user_id)
 client=_client()
 if client:
  try:
   client.setex(key,settings.REDIS_CACHE_TTL_SECONDS,json.dumps(response))
   return
  except Exception as exc:
   log.warning("Redis cache write failed: %s",exc)
   global _redis
   _redis=False
 with _lock:
  _memory[key]=response

def invalidate_user_cache(user_id:str|int)->None:
 prefix=_user_prefix(user_id)
 client=_client()
 if client:
  try:
   keys=list(client.scan_iter(match=f"{prefix}*"))
   if keys:
    client.delete(*keys)
  except Exception as exc:
   log.warning("Redis cache invalidation failed for user %s: %s",user_id,exc)
   global _redis
   _redis=False
 with _lock:
  for key in [key for key in _memory if key.startswith(prefix)]:
   _memory.pop(key,None)
