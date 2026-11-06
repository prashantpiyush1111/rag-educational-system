from io import BytesIO
from docx import Document
import pytest
from app.rag.ingestion.loader import load_document

def test_load_txt():
    assert load_document(b"hello\nworld", ".txt") == "hello\nworld"

def test_reject_empty_text():
    with pytest.raises(ValueError):
        load_document(b"  ", ".txt")

def test_reject_unsupported_extension():
    with pytest.raises(ValueError):
        load_document(b"test", ".exe")

def test_load_docx():
    document = Document()
    document.add_paragraph("Learning")
    stream = BytesIO()
    document.save(stream)
    assert load_document(stream.getvalue(), ".docx") == "Learning"
