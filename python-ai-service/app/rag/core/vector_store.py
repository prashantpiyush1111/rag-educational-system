import hashlib
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
        _collection = get_client().get_or_create_collection(name=settings.CHROMA_COLLECTION_NAME)
    return _collection
def store_embeddings(embedded_chunks: list[dict], user_id: str | int | None = None):
    collection = get_collection()
    ids=[]; embeddings=[]; documents=[]; metadatas=[]
    for chunk in embedded_chunks:
        source=chunk["source"]
        original_id=str(chunk.get("id") or hashlib.sha256((source+chunk["text"]).encode()).hexdigest())
        uid=str(user_id if user_id is not None else chunk.get("user_id",""))
        ids.append(hashlib.sha256(f"{uid}:{original_id}".encode()).hexdigest())
        embeddings.append(chunk["embedding"]); documents.append(chunk["text"])
        metadatas.append({"source":source,"user_id":uid,"chunk_id":original_id})
    if ids: collection.upsert(ids=ids,embeddings=embeddings,documents=documents,metadatas=metadatas)
def list_documents(user_id: str | int | None = None):
    collection=get_collection()
    result=collection.get(where={"user_id":str(user_id)},include=["metadatas"]) if user_id is not None else collection.get(include=["metadatas"])
    counts={}
    for metadata in result.get("metadatas") or []:
        source=metadata.get("source")
        if source: counts[source]=counts.get(source,0)+1
    return [{"id":source,"filename":source,"chunks_created":count} for source,count in sorted(counts.items())]
def delete_document(filename: str,user_id: str | int):
    collection=get_collection()
    collection.delete(where={"$and":[{"source":filename},{"user_id":str(user_id)}]})
