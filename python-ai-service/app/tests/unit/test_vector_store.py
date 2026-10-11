from unittest.mock import patch
from app.rag.core.vector_store import store_embeddings, list_documents, delete_document

def test_upsert_stores_user_metadata():
    chunks=[{"id":"file:0","source":"file.txt","text":"hello","embedding":[0.1,0.2]}]
    with patch("app.rag.core.vector_store.get_collection") as get_collection:
        store_embeddings(chunks,user_id="42")
        args=get_collection.return_value.upsert.call_args.kwargs
        assert args["metadatas"][0]["user_id"]=="42"

def test_list_documents_includes_chunk_count():
    with patch("app.rag.core.vector_store.get_collection") as get_collection:
        get_collection.return_value.get.return_value={"metadatas":[{"source":"a.pdf"},{"source":"a.pdf"}]}
        result=list_documents("42")
        assert result[0]["chunks_created"]==2

def test_delete_is_scoped_by_owner():
    with patch("app.rag.core.vector_store.get_collection") as get_collection:
        delete_document("a.pdf","42")
        assert get_collection.return_value.delete.call_args.kwargs["where"] == {"$and":[{"source":"a.pdf"},{"user_id":"42"}]}
