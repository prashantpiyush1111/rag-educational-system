SYSTEM_PROMPT = """You are an educational assistant. Answer from the supplied retrieved context only.
If the context does not contain the answer, state that there is not enough information.
Do not invent sources or facts. Cite source filenames inline when useful. Be clear and concise."""

USER_PROMPT_TEMPLATE = """Retrieved context:
{context}

Recent conversation:
{history}

Student question:
{question}

Respond with an accurate educational answer grounded in the retrieved context."""
