import logging
from time import perf_counter
from fastapi import APIRouter, HTTPException, Depends, Header
from starlette.concurrency import run_in_threadpool
from app.models.request import QueryRequest
from app.models.response import QueryResponse
from app.rag.core.retriever import retrieve_relevant_chunks
from app.rag.llm.generator import generate_answer
from app.api.dependencies.auth import verify_api_key
from app.services.cache import get_cached_response, set_cached_response
from app.rag.reranker.cross_encoder import rerank_chunks
from app.rag.memory.conversation import get_history, append_turn, format_history
from app.api.metrics import REQUEST_LATENCY_SECONDS

log = logging.getLogger(__name__)
router = APIRouter()

@router.post("/", response_model=QueryResponse, dependencies=[Depends(verify_api_key)])
async def query_documents(request: QueryRequest, x_user_id: str | None = Header(default=None)):
    if not x_user_id:
        raise HTTPException(status_code=400, detail="Missing X-User-Id")
    started = perf_counter()
    with REQUEST_LATENCY_SECONDS.time():
        try:
            cached = get_cached_response(request.question, request.top_k)
            if cached is not None:
                return QueryResponse(**cached)
            history = get_history(request.session_id)
            relevant = await run_in_threadpool(retrieve_relevant_chunks, request.question, max(request.top_k, 12), x_user_id)
            relevant = rerank_chunks(request.question, relevant, request.top_k)
            if not relevant:
                raise HTTPException(status_code=404, detail="No relevant documents found")
            answer = await run_in_threadpool(generate_answer, request.question, relevant, format_history(history))
            response_data = {"answer": answer, "sources": sorted({chunk["source"] for chunk in relevant}), "question": request.question}
            set_cached_response(request.question, request.top_k, response_data)
            append_turn(request.session_id, request.question, answer)
            return QueryResponse(**response_data)
        except HTTPException:
            raise
        except Exception as exc:
            log.exception("RAG query failed")
            name = type(exc).__name__.lower()
            if "rate" in name or "timeout" in name or "api" in name or "connection" in name:
                raise HTTPException(status_code=503, detail="AI provider is temporarily unavailable") from exc
            raise HTTPException(status_code=502, detail="Unable to generate an answer at this time") from exc
