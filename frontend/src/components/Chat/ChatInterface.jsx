import React,{useState,useRef,useEffect} from "react";
import ChatMessage from "./ChatMessage";
import api,{responseDetail} from "../../services/api";
import {sendFeedback} from "../../services/feedback";

export default function ChatInterface(){
 const [messages,setMessages]=useState([]);const [question,setQuestion]=useState("");const [loading,setLoading]=useState(false);const [error,setError]=useState("");const chatBoxRef=useRef(null);
 useEffect(()=>{if(chatBoxRef.current)chatBoxRef.current.scrollTop=chatBoxRef.current.scrollHeight;},[messages]);
 async function rateAnswer(messageId,rating){
  const msg=messages.find(m=>m.id===messageId);
  if(!msg?.queryLogId){setMessages(prev=>prev.map(m=>m.id===messageId?{...m,feedbackStatus:"Feedback unavailable for this response"}:m));return;}
  try{await sendFeedback(msg.queryLogId,rating);setMessages(prev=>prev.map(m=>m.id===messageId?{...m,feedbackStatus:"Thanks for your feedback"}:m));}
  catch(err){setMessages(prev=>prev.map(m=>m.id===messageId?{...m,feedbackStatus:responseDetail(err,"Feedback failed")}:m));}
 }
 async function askQuestion(event){
  event?.preventDefault();const trimmed=question.trim();if(!trimmed||loading)return;
  setMessages(prev=>[...prev,{id:`user-${Date.now()}`,role:"user",text:trimmed}]);setQuestion("");setError("");setLoading(true);
  try{
   const {data}=await api.post("/query",{question:trimmed,topK:4});
   setMessages(prev=>[...prev,{id:`assistant-${Date.now()}`,role:"assistant",text:data.answer,sources:data.sources||[],question:data.question||trimmed,queryLogId:data.queryLogId}]);
  }catch(err){setError(responseDetail(err,"Could not connect to the AI service."));}
  finally{setLoading(false);}
 }
 return <div className="chat-interface">
  <div className="chat-interface__box" ref={chatBoxRef} aria-live="polite">
   {messages.length===0&&<p className="chat-interface__empty">Ask a question about your uploaded documents to get started.</p>}
   {messages.map(msg=><ChatMessage key={msg.id} {...msg} onFeedback={msg.role==="assistant"?(rating)=>rateAnswer(msg.id,rating):undefined} feedbackStatus={msg.feedbackStatus}/>)}
   {loading&&<p className="chat-interface__typing" role="status">Thinking…</p>}
  </div>
  {error&&<p role="alert" className="chat-error">{error}</p>}
  <form className="chat-interface__input-row" onSubmit={askQuestion}>
   <input type="text" value={question} onChange={e=>setQuestion(e.target.value)} onKeyDown={e=>{if(e.key==="Enter"&&!e.shiftKey){e.preventDefault();askQuestion(e);}}} placeholder="Ask a question about your documents..." disabled={loading} aria-label="Question"/>
   <button type="submit" disabled={loading||!question.trim()}>{loading?"Thinking…":"Ask"}</button>
  </form>
 </div>;
}
