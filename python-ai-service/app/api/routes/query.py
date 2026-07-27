from fastapi import APIRouter, HTTPException
from app.models.request import QueryRequest
from app.models.response import QueryResponse
from app.rag.core.retriever import retrieve_relevant_chunks
from app.rag.llm.generator import generate_answer

router = APIRouter()

@router.post("/", response_model=QueryResponse)
async def query_documents(request: QueryRequest):
    try:
        relevant_chunks = retrieve_relevant_chunks(
            query=request.question,
            top_k=request.top_k or 4
        )

        if not relevant_chunks:
            raise HTTPException(status_code=404, detail="No relevant documents found")

        answer = generate_answer(
            question=request.question,
            context_chunks=relevant_chunks
        )

        return QueryResponse(
            answer=answer,
            sources=[chunk["source"] for chunk in relevant_chunks],
            question=request.question
        )

    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))