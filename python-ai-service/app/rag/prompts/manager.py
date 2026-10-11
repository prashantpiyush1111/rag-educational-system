from app.rag.prompts.templates import SYSTEM_PROMPT, USER_PROMPT_TEMPLATE

def build_prompts(question: str, context_chunks: list[dict], history: str = "") -> tuple[str, str]:
    context = "\n\n".join(f"[Source: {chunk.get('source', 'unknown')}]\n{chunk.get('text', '')}" for chunk in context_chunks)
    return SYSTEM_PROMPT, USER_PROMPT_TEMPLATE.format(context=context, history=history or "(no previous conversation)", question=question)
