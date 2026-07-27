from fastapi import APIRouter, UploadFile, File, HTTPException
from app.rag.ingestion.loader import load_document
from app.rag.ingestion.processor import chunk_document
from app.rag.embeddings.encoder import embed_chunks
from app.rag.core.vector_store import store_embeddings

router = APIRouter()

@router.post("/upload")
async def upload_document(file: UploadFile = File(...)):
    try:
        allowed_types = [".pdf", ".txt", ".docx"]
        file_ext = "." + file.filename.split(".")[-1].lower()

        if file_ext not in allowed_types:
            raise HTTPException(
                status_code=400,
                detail=f"Unsupported file type. Allowed: {allowed_types}"
            )

        content = await file.read()

        text = load_document(content, file_ext)

        chunks = chunk_document(text, source=file.filename)

        embedded_chunks = embed_chunks(chunks)

        store_embeddings(embedded_chunks)

        return {
            "status": "success",
            "filename": file.filename,
            "chunks_created": len(chunks)
        }

    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))