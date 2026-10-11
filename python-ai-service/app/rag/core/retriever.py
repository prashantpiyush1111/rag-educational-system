from app.rag.core.vector_store import get_collection
from app.rag.embeddings.encoder import embed_text

def retrieve_relevant_chunks(query: str, top_k: int = 4, user_id: str | int | None = None) -> list[dict]:
    collection = get_collection()
    query_embedding = embed_text(query)
    options = {"query_embeddings": [query_embedding], "n_results": top_k}
    if user_id is not None:
        options["where"] = {"user_id": str(user_id)}
    results = collection.query(**options)
    chunks = []
    documents = (results.get("documents") or [[]])[0]
    metadatas = (results.get("metadatas") or [[]])[0]
    distances = (results.get("distances") or [[]])[0]
    for index, (doc, meta) in enumerate(zip(documents, metadatas)):
        chunks.append({"text": doc, "source": meta.get("source", "unknown"), "distance": distances[index] if index < len(distances) else None})
    return chunks
