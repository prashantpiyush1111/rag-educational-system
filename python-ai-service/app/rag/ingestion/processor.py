from langchain_text_splitters import RecursiveCharacterTextSplitter

def chunk_document(text: str, source: str, chunk_size: int = 1000, chunk_overlap: int = 150) -> list[dict]:
    if not text or not text.strip():
        raise ValueError("Document contains no extractable text")
    splitter = RecursiveCharacterTextSplitter(chunk_size=chunk_size, chunk_overlap=chunk_overlap)
    chunks = splitter.split_text(text)
    return [{"id": f"{source}:{index}", "source": source, "text": chunk} for index, chunk in enumerate(chunks)]
