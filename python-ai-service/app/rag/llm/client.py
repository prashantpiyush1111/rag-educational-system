from groq import Groq
from app.config.settings import settings

_client = None

def get_groq_client():
    global _client
    if not settings.GROQ_API_KEY:
        raise RuntimeError("GROQ_API_KEY is not configured")
    if _client is None:
        _client = Groq(api_key=settings.GROQ_API_KEY, timeout=20.0, max_retries=1)
    return _client
