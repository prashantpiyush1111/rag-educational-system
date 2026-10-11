from unittest.mock import patch
from app.rag.core.retriever import retrieve_relevant_chunks

@patch("app.rag.core.retriever.embed_text", return_value=[0.1,0.2])
@patch("app.rag.core.retriever.get_collection")
def test_retrieve_filters_by_user(mock_collection, mock_embed):
    mock_collection.return_value.query.return_value = {"documents":[["chunk"]],"metadatas":[[{"source":"a.pdf"}]],"distances":[[0.2]]}
    result=retrieve_relevant_chunks("question",top_k=2,user_id="99")
    assert result[0]["source"]=="a.pdf"
    assert mock_collection.return_value.query.call_args.kwargs["where"] == {"user_id":"99"}
