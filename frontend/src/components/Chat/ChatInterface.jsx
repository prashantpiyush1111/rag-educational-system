import React, { useState, useRef, useEffect } from "react";
import ChatMessage from "./ChatMessage";

const API_BASE =
  import.meta.env.VITE_API_BASE || "http://127.0.0.1:8000";

/**
 * components/Chat/ChatInterface.jsx
 * Owns chat state (messages, loading) and talks to the Python
 * RAG service's /query/ endpoint. Rendering of each message is
 * delegated to ChatMessage.jsx.
 */

export default function ChatInterface() {
  const [messages, setMessages] = useState([]);
  const [question, setQuestion] = useState("");
  const [loading, setLoading] = useState(false);
  const chatBoxRef = useRef(null);

  useEffect(() => {
    if (chatBoxRef.current) {
      chatBoxRef.current.scrollTop = chatBoxRef.current.scrollHeight;
    }
  }, [messages]);

  async function askQuestion() {
    const trimmed = question.trim();
    if (!trimmed || loading) return;

    setMessages((prev) => [...prev, { role: "user", text: trimmed }]);
    setQuestion("");
    setLoading(true);

    try {
      const res = await fetch(`${API_BASE}/query/`, {
        method: "POST",
        headers: {
    "Content-Type": "application/json",
    "X-API-Key": "Prashant",
},
        body: JSON.stringify({ question: trimmed, top_k: 4 }),
      });
      const data = await res.json();

      if (res.ok) {
        setMessages((prev) => [
          ...prev,
          { role: "assistant", text: data.answer, sources: data.sources },
        ]);
      } else {
        setMessages((prev) => [
          ...prev,
          { role: "assistant", text: `Error: ${data.detail || "Something went wrong"}` },
        ]);
      }
    } catch (err) {
      setMessages((prev) => [
        ...prev,
        { role: "assistant", text: "Could not connect to server. Is it running?" },
      ]);
    } finally {
      setLoading(false);
    }
  }

  function handleKeyPress(e) {
    if (e.key === "Enter") askQuestion();
  }

  return (
    <div className="chat-interface">
      <div className="chat-interface__box" ref={chatBoxRef}>
        {messages.length === 0 && (
          <p className="chat-interface__empty">
            Ask a question about your uploaded documents to get started.
          </p>
        )}
        {messages.map((msg, i) => (
          <ChatMessage key={i} role={msg.role} text={msg.text} sources={msg.sources} />
        ))}
        {loading && <p className="chat-interface__typing">Thinking…</p>}
      </div>

      <div className="chat-interface__input-row">
        <input
          type="text"
          value={question}
          onChange={(e) => setQuestion(e.target.value)}
          onKeyPress={handleKeyPress}
          placeholder="Ask a question about your documents..."
          disabled={loading}
        />
        <button onClick={askQuestion} disabled={loading || !question.trim()}>
          Ask
        </button>
      </div>
    </div>
  );
}