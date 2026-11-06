from pydantic_settings import BaseSettings, SettingsConfigDict
from pydantic import Field

class Settings(BaseSettings):
    model_config = SettingsConfigDict(env_file=".env", extra="ignore")
    GROQ_API_KEY: str = ""
    GROQ_MODEL: str = "llama-3.1-8b-instant"
    CHROMA_DB_PATH: str = "./chroma_data"
    CHROMA_COLLECTION_NAME: str = "educational_documents"
    INTERNAL_API_KEY: str = "local-only-internal-key-change-me"
    REDIS_HOST: str = "localhost"
    REDIS_PORT: int = 6379
    REDIS_CACHE_TTL_SECONDS: int = 900
    CORS_ORIGINS: str = "http://localhost:8080,http://localhost:5173"
    MAX_UPLOAD_BYTES: int = 10485760
    RERANKER_ENABLED: bool = False
    RERANKER_MODEL: str = "cross-encoder/ms-marco-MiniLM-L-6-v2"
    RERANKER_TOP_N: int = 12
    CONVERSATION_HISTORY_TURNS: int = 6

    @property
    def cors_origins(self) -> list[str]:
        return [origin.strip() for origin in self.CORS_ORIGINS.split(",") if origin.strip()]

settings = Settings()
