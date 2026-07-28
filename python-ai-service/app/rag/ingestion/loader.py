from io import BytesIO
from pypdf import PdfReader

def load_document(content: bytes, file_ext: str) -> str:
    if file_ext == ".pdf":
        return _load_pdf(content)
    elif file_ext == ".txt":
        return _load_txt(content)
    elif file_ext == ".docx":
        return _load_docx(content)
    else:
        raise ValueError(f"Unsupported file type: {file_ext}")

def _load_pdf(content: bytes) -> str:
    reader = PdfReader(BytesIO(content))
    text = ""
    for page in reader.pages:
        text += page.extract_text() or ""
    return text

def _load_txt(content: bytes) -> str:
    return content.decode("utf-8")

def _load_docx(content: bytes) -> str:
    from docx import Document
    doc = Document(BytesIO(content))
    return "\n".join(para.text for para in doc.paragraphs)