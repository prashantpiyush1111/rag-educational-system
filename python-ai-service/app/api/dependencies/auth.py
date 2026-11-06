import hmac
from fastapi import Header, HTTPException
from app.config.settings import settings

async def verify_api_key(x_api_key: str | None = Header(default=None)):
    if not x_api_key or not hmac.compare_digest(x_api_key, settings.INTERNAL_API_KEY):
        raise HTTPException(status_code=401, detail="Invalid or missing API key")
    return True
