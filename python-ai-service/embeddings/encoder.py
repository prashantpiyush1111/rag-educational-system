from app.rag.embeddings.model import get_embedding_model

def embed_text(text: str) -> list[float]:
    model = get_embedding_model()
    embedding = model.encode(text)
    return embedding.tolist()

def embed_chunks(chunks: list[dict]) -> list[dict]:
    model = get_embedding_model()
    texts = [chunk["text"] for chunk in chunks]
    embeddings = model.encode(texts)

    embedded_chunks = []
    for i, chunk in enumerate(chunks):
        embedded_chunks.append({
            "id": f"{chunk['source']}_{chunk['chunk_index']}",
            "text": chunk["text"],
            "source": chunk["source"],
            "embedding": embeddings[i].tolist()
        })

    return embedded_chunks