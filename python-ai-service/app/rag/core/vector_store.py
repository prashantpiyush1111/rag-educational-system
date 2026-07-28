import chromadb
from app.config.settings import settings

_client = None
_collection = None

def get_client():
    global _client
    if _client is None:
        _client = chromadb.PersistentClient(path=settings.CHROMA_DB_PATH)
    return _client

def get_collection():
    global _collection
    if _collection is None:
        client = get_client()
        _collection = client.get_or_create_collection(
            name=settings.CHROMA_COLLECTION_NAME
        )
    return _collection

def store_embeddings(embedded_chunks: list[dict]):
    collection = get_collection()

    ids = [chunk["id"] for chunk in embedded_chunks]
    embeddings = [chunk["embedding"] for chunk in embedded_chunks]
    documents = [chunk["text"] for chunk in embedded_chunks]
    metadatas = [{"source": chunk["source"]} for chunk in embedded_chunks]

    collection.add(
        ids=ids,
        embeddings=embeddings,
        documents=documents,
        metadatas=metadatas
    )