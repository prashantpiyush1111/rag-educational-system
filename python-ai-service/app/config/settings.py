from pydantic_settings import BaseSettings

class Settings(BaseSettings):
    GROQ_API_KEY: str
    GROQ_MODEL: str = "llama-3.3-70b-versatile"

    EMBEDDING_MODEL_NAME: str = "sentence-transformers/all-MiniLM-L6-v2"

    CHROMA_DB_PATH: str = "./data/chroma_db"
    CHROMA_COLLECTION_NAME: str = "educational_documents"

    CHUNK_SIZE: int = 1000
    CHUNK_OVERLAP: int = 200
    TOP_K_RESULTS: int = 4

    APP_HOST: str = "0.0.0.0"
    APP_PORT: int = 8000

    INTERNAL_API_KEY: str = "change-me"

    class Config:
        env_file = ".env"

settings = Settings()