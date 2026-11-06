import hashlib
import logging
from fastapi import APIRouter,HTTPException,Depends,Header
from starlette.concurrency import run_in_threadpool
from app.models.request import QueryRequest
from app.models.response import QueryResponse
from app.rag.core.retriever import retrieve_relevant_chunks
from app.rag.llm.generator import generate_answer
from app.api.dependencies.auth import verify_api_key
from app.services.cache import get_cached_response,set_cached_response
from app.rag.reranker.cross_encoder import rerank_chunks
from app.rag.memory.conversation import get_history,append_turn,format_history
from app.api.metrics import REQUEST_LATENCY_SECONDS

log=logging.getLogger(__name__);router=APIRouter()
@router.post("/",response_model=QueryResponse,dependencies=[Depends(verify_api_key)])
async def query_documents(request:QueryRequest,x_user_id:str|None=Header(default=None)):
 if not x_user_id:raise HTTPException(status_code=400,detail="Missing X-User-Id")
 session=request.session_id or hashlib.sha256(f"{x_user_id}:{request.question.strip().lower()}".encode()).hexdigest()[:32]
 try:
  cached=get_cached_response(request.question,request.top_k,x_user_id)
  if cached is not None:return QueryResponse(**cached)
  with REQUEST_LATENCY_SECONDS.time():
   history=get_history(session)
   chunks=await run_in_threadpool(retrieve_relevant_chunks,request.question,max(request.top_k,12),x_user_id)
   chunks=rerank_chunks(request.question,chunks,request.top_k)
   if not chunks:raise HTTPException(status_code=404,detail="No relevant documents found")
   answer=await run_in_threadpool(generate_answer,request.question,chunks,format_history(history))
   data={"answer":answer,"sources":sorted({c["source"] for c in chunks}),"question":request.question}
   set_cached_response(request.question,request.top_k,data,x_user_id)
   append_turn(session,request.question,answer)
   return QueryResponse(**data)
 except HTTPException:raise
 except Exception as exc:
  log.exception("RAG query failed")
  text=str(exc).lower()
  status=503 if any(k in text for k in ("rate limit","timeout","connection","temporarily unavailable")) else 502
  raise HTTPException(status_code=status,detail="AI service temporarily unavailable" if status==503 else "Unable to generate an answer at this time") from exc
