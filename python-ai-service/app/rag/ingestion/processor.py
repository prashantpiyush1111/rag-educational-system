from langchain_text_splitters import RecursiveCharacterTextSplitter
from app.config.settings import settings

def chunk_document(text: str, source: str) -> list[dict]:
    splitter = RecursiveCharacterTextSplitter(
        chunk_size=settings.CHUNK_SIZE,
        chunk_overlap=settings.CHUNK_OVERLAP,
        separators=["\n\n", "\n", ". ", " ", ""]
    )

    text_chunks = splitter.split_text(text)

    chunks = []
    for i, chunk_text in enumerate(text_chunks):
        chunks.append({
            "text": chunk_text,
            "source": source,
            "chunk_index": i
        })

    return chunks