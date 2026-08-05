import React from "react";
import ChatInterface from "../components/Chat/ChatInterface";

/**
 * Chat.jsx — Route: "/chat"
 * Page shell for the chat experience. Keeps layout concerns here,
 * delegates all chat logic (messages, API calls, state) to
 * components/Chat/ChatInterface.jsx.
 */

export default function Chat() {
  return (
    <div className="chat-page">
      <header className="chat-page__header">
        <h1 className="chat-page__title">Ask your documents</h1>
        <p className="chat-page__subtitle">
          Answers are generated only from the material you've uploaded.
        </p>
      </header>

      <ChatInterface />
    </div>
  );
}