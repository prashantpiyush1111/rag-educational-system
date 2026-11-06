import React, { useState } from "react";
import { documentsApi } from "../../services/documents";
import { responseDetail } from "../../services/api";

export default function DocumentUpload({ onUploaded }) {
  const [file,setFile]=useState(null);const [progress,setProgress]=useState(0);const [loading,setLoading]=useState(false);const [message,setMessage]=useState("");const [error,setError]=useState("");
  async function submit(event){event.preventDefault();if(!file)return;setLoading(true);setProgress(0);setError("");setMessage("");
    try{const result=await documentsApi.upload(file,setProgress);setMessage(`Uploaded ${result.filename} (${result.chunks_created} chunks)`);setFile(null);event.currentTarget?.reset?.();onUploaded?.();}
    catch(err){setError(responseDetail(err,"Upload failed"));}
    finally{setLoading(false);}
  }
  return <form className="document-upload" onSubmit={submit}>
    <label>Upload a document (PDF, TXT, DOCX)<input type="file" accept=".pdf,.txt,.docx" onChange={e=>setFile(e.target.files?.[0]||null)} disabled={loading} required/></label>
    {loading&&<div><progress value={progress} max="100" aria-label="Upload progress"/><span>{progress}%</span></div>}
    {error&&<p role="alert">{error}</p>}{message&&<p role="status">{message}</p>}
    <button type="submit" disabled={!file||loading}>{loading?"Uploading…":"Upload document"}</button>
  </form>;
}
