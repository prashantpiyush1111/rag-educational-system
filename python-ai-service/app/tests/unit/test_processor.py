import pytest
from app.rag.ingestion.processor import chunk_document

def test_chunk_document_retains_source():
    chunks = chunk_document("word " * 1000, "study.txt", chunk_size=100, chunk_overlap=10)
    assert chunks and all(c["source"] == "study.txt" for c in chunks)
    assert chunks[0]["id"] == "study.txt:0"

def test_reject_blank_document():
    with pytest.raises(ValueError):
        chunk_document("  ", "empty.txt")
