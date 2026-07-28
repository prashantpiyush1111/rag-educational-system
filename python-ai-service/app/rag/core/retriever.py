from app.rag.core.vector_store import get_collection
from app.rag.embeddings.encoder import embed_text

def retrieve_relevant_chunks(query: str, top_k: int = 4) -> list[dict]:
    collection = get_collection()

    query_embedding = embed_text(query)

    results = collection.query(
        query_embeddings=[query_embedding],
        n_results=top_k
    )

    chunks = []
    documents = results.get("documents", [[]])[0]
    metadatas = results.get("metadatas", [[]])[0]

    for doc, meta in zip(documents, metadatas):
        chunks.append({
            "text": doc,
            "source": meta.get("source", "unknown")
        })

    return chunks