import React from "react";

/**
 * components/Chat/ChatMessage.jsx
 * Renders a single chat message — user or assistant — with an
 * optional list of source documents shown under assistant replies.
 */

export default function ChatMessage({ role, text, sources }) {
  const isUser = role === "user";
  const uniqueSources = sources ? [...new Set(sources)] : [];

  return (
    <div className={`chat-message chat-message--${role}`}>
      <div className="chat-message__role">{isUser ? "You" : "Assistant"}</div>
      <div className="chat-message__text">{text}</div>

      {uniqueSources.length > 0 && (
        <div className="chat-message__sources">
          Sources: {uniqueSources.join(", ")}
        </div>
      )}
    </div>
  );
}