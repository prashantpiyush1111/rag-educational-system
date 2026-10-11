from io import BytesIO
from pathlib import Path
from pypdf import PdfReader
from docx import Document as DocxDocument

def load_document(content: bytes, file_ext: str) -> str:
    extension = file_ext.lower()
    if extension == ".pdf":
        reader = PdfReader(BytesIO(content))
        text = "\n".join((page.extract_text() or "") for page in reader.pages)
    elif extension == ".txt":
        text = content.decode("utf-8", errors="replace")
    elif extension == ".docx":
        document = DocxDocument(BytesIO(content))
        text = "\n".join(p.text for p in document.paragraphs)
    else:
        raise ValueError(f"Unsupported file type: {extension}")
    if not text.strip():
        raise ValueError("Document contains no extractable text")
    return text
