from app.rag.llm.client import get_openai_client
from app.config.settings import settings

def generate_answer(question: str, context_chunks: list[dict]) -> str:
    client = get_openai_client()

    context_text = "\n\n".join(
        f"[Source: {chunk['source']}]\n{chunk['text']}" for chunk in context_chunks
    )

    system_prompt = (
        "You are an educational assistant. Answer the student's question "
        "using ONLY the provided context. If the answer is not in the context, "
        "say you don't have enough information. Be clear and concise."
    )

    user_prompt = f"Context:\n{context_text}\n\nQuestion: {question}\n\nAnswer:"

    response = client.chat.completions.create(
        model=settings.OPENAI_CHAT_MODEL,
        messages=[
            {"role": "system", "content": system_prompt},
            {"role": "user", "content": user_prompt}
        ],
        temperature=0.3
    )

    return response.choices[0].message.content