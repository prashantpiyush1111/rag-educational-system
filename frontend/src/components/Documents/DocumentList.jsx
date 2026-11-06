import React, { useEffect, useState } from "react";
import { documentsApi } from "../../services/documents";
import { responseDetail } from "../../services/api";

export default function DocumentList({ refreshKey=0 }) {
  const [documents,setDocuments]=useState([]);const [loading,setLoading]=useState(true);const [error,setError]=useState("");
  async function load(){setLoading(true);setError("");try{setDocuments(await documentsApi.list());}catch(err){setError(responseDetail(err,"Could not load documents"));}finally{setLoading(false);}}
  useEffect(()=>{load();},[refreshKey]);
  async function remove(id){try{await documentsApi.remove(id);setDocuments(prev=>prev.filter(d=>d.id!==id));}catch(err){setError(responseDetail(err,"Could not delete document"));}}
  if(loading)return <p role="status">Loading documents…</p>;
  return <section className="document-list"><h2>Your documents</h2>{error&&<p role="alert">{error}</p>}
    {documents.length===0?<p>No documents uploaded yet.</p>:<ul>{documents.map(doc=><li key={doc.id}><span>{doc.filename} ({doc.chunks_created??0} chunks)</span><button type="button" onClick={()=>remove(doc.id)} aria-label={`Delete ${doc.filename}`}>Delete</button></li>)}</ul>}
  </section>;
}
