from app.rag.core.retriever import retrieve_relevant_chunks
from app.rag.llm.generator import generate_answer

class RAGAgent:
    def __init__(self, top_k: int = 4):
        self.top_k = top_k

    def run(self, question: str) -> dict:
        relevant_chunks = retrieve_relevant_chunks(query=question, top_k=self.top_k)

        if not relevant_chunks:
            return {
                "answer": "I couldn't find relevant information in the documents to answer this question.",
                "sources": []
            }

        answer = generate_answer(question=question, context_chunks=relevant_chunks)

        return {
            "answer": answer,
            "sources": [chunk["source"] for chunk in relevant_chunks]
        }