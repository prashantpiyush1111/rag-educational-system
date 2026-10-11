from collections import defaultdict, deque
from threading import RLock
from app.config.settings import settings

_history: dict[str, deque] = defaultdict(lambda: deque(maxlen=settings.CONVERSATION_HISTORY_TURNS * 2))
_lock = RLock()

def get_history(session_id: str | None) -> list[dict[str, str]]:
    if not session_id:
        return []
    with _lock:
        return list(_history[session_id])

def append_turn(session_id: str | None, question: str, answer: str) -> None:
    if not session_id:
        return
    with _lock:
        history = _history[session_id]
        history.append({"role": "user", "content": question})
        history.append({"role": "assistant", "content": answer})

def format_history(history: list[dict[str, str]]) -> str:
    return "\n".join(f"{item['role']}: {item['content']}" for item in history)
