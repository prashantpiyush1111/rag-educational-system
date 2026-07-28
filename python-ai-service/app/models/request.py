from pydantic import BaseModel, Field
from typing import Optional

class QueryRequest(BaseModel):
    question: str = Field(..., min_length=1, description="The question to ask")
    top_k: Optional[int] = Field(default=4, ge=1, le=10, description="Number of chunks to retrieve")