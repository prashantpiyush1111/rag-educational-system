import React from "react";

export default function ChatMessage({ role, text, sources = [], onFeedback, feedbackStatus }) {
  const assistant = role === "assistant";
  return <article className={`chat-message chat-message--${role}`}>
    <div className="chat-message__content"><strong>{assistant ? "Assistant" : "You"}</strong><p>{text}</p></div>
    {assistant && sources?.length > 0 && <div className="chat-message__sources"><strong>Sources</strong><ul>{sources.map((source,index)=><li key={`${source}-${index}`}>{source}</li>)}</ul></div>}
    {assistant && onFeedback && <div className="chat-feedback" aria-label="Rate this answer">
      <span>Helpful?</span>{[1,2,3,4,5].map(rating=><button type="button" key={rating} onClick={()=>onFeedback(rating)} aria-label={`Rate ${rating} out of 5`} disabled={Boolean(feedbackStatus)}>{rating}</button>)}
      {feedbackStatus && <small role="status">{feedbackStatus}</small>}
    </div>}
  </article>;
}
