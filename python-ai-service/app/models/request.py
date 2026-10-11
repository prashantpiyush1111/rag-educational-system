from pydantic import BaseModel, Field, ConfigDict

class QueryRequest(BaseModel):
    model_config = ConfigDict(extra="ignore")
    question: str = Field(min_length=1, max_length=8000)
    top_k: int = Field(default=4, ge=1, le=20)
    session_id: str | None = Field(default=None, max_length=128)
