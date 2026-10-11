"""Conversation memory is implemented in app.rag.memory.conversation.
This module intentionally exposes no vector-memory store because the
application uses a shared Chroma collection for user documents only.
"""