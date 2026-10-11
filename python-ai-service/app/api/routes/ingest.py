from fastapi import APIRouter, UploadFile, File, HTTPException, Depends, Header
from app.config.settings import settings
from app.rag.ingestion.loader import load_document
from app.rag.ingestion.processor import chunk_document
from app.rag.embeddings.encoder import embed_chunks
from app.rag.core.vector_store import store_embeddings, list_documents, delete_document
from app.api.dependencies.auth import verify_api_key

router = APIRouter()

@router.post("/upload", dependencies=[Depends(verify_api_key)])
async def upload_document(file: UploadFile = File(...), user_id: str | None = None, x_user_id: str | None = Header(default=None)):
    filename = file.filename or "upload.txt"
    allowed_types = {".pdf", ".txt", ".docx"}
    extension = "." + filename.rsplit(".", 1)[-1].lower() if "." in filename else ""
    if extension not in allowed_types:
        raise HTTPException(status_code=400, detail="Unsupported file type. Allowed: .pdf, .txt, .docx")
    limit = settings.MAX_UPLOAD_BYTES
    content = await file.read(limit + 1)
    if len(content) > limit:
        raise HTTPException(status_code=413, detail="Uploaded file exceeds the size limit")
    if not content:
        raise HTTPException(status_code=400, detail="Uploaded file is empty")
    owner = x_user_id or user_id
    if not owner:
        raise HTTPException(status_code=400, detail="Missing user_id")
    try:
        text = load_document(content, extension)
        chunks = chunk_document(text, source=filename)
        embedded_chunks = embed_chunks(chunks)
        store_embeddings(embedded_chunks, user_id=owner)
        return {"status": "success", "filename": filename, "chunks_created": len(embedded_chunks)}
    except ValueError as exc:
        raise HTTPException(status_code=400, detail=str(exc)) from exc
    except HTTPException:
        raise
    except Exception as exc:
        raise HTTPException(status_code=500, detail="Could not process uploaded document") from exc

@router.get("/", dependencies=[Depends(verify_api_key)])
async def get_documents(x_user_id: str | None = Header(default=None)):
    if not x_user_id:
        raise HTTPException(status_code=400, detail="Missing X-User-Id")
    return list_documents(x_user_id)

@router.delete("/{filename}", dependencies=[Depends(verify_api_key)])
async def remove_document(filename: str, x_user_id: str | None = Header(default=None)):
    if not x_user_id:
        raise HTTPException(status_code=400, detail="Missing X-User-Id")
    delete_document(filename, x_user_id)
    return {"status": "deleted", "filename": filename}
