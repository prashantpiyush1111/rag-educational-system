from app.rag.llm.client import get_groq_client
from app.config.settings import settings
from app.rag.prompts.manager import build_prompts

def generate_answer(question: str, context_chunks: list[dict], history: str = "") -> str:
    client = get_groq_client()
    system_prompt, user_prompt = build_prompts(question, context_chunks, history)
    response = client.chat.completions.create(
        model=settings.GROQ_MODEL,
        messages=[{"role": "system", "content": system_prompt}, {"role": "user", "content": user_prompt}],
        temperature=0.3,
    )
    answer = response.choices[0].message.content
    if not answer:
        raise RuntimeError("AI service returned an empty answer")
    return answer
