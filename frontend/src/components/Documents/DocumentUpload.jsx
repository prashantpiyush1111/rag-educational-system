import React, { useState, useRef } from "react";

const API_BASE =
  import.meta.env.VITE_API_BASE || "http://127.0.0.1:8000";

/**
 * components/Documents/DocumentUpload.jsx
 * Handles selecting a file and uploading it to the Python RAG
 * service's /documents/upload endpoint. Calls onUploaded() after a
 * successful upload so a parent (e.g. DocumentList) can refresh.
 */

export default function DocumentUpload({ onUploaded }) {
  const [status, setStatus] = useState("");
  const [uploading, setUploading] = useState(false);
  const fileInputRef = useRef(null);

  async function handleUpload() {
    const file = fileInputRef.current?.files?.[0];
    if (!file) {
      setStatus("Please select a file first.");
      return;
    }

    setUploading(true);
    setStatus("Uploading and processing...");

    const formData = new FormData();
    formData.append("file", file);

    try {
const res = await fetch(`${API_BASE}/documents/upload`, {
    method: "POST",
    headers: {
        "X-API-Key": "Prashant",
    },
    body: formData,
});
      const data = await res.json();

      if (res.ok) {
        setStatus(`✅ ${data.filename} uploaded — ${data.chunks_created} chunks created.`);
        if (fileInputRef.current) fileInputRef.current.value = "";
        if (onUploaded) onUploaded(data);
      } else {
        setStatus(`❌ Error: ${data.detail || "Upload failed"}`);
      }
    } catch (err) {
      setStatus("❌ Could not connect to server. Is it running?");
    } finally {
      setUploading(false);
    }
  }

  return (
    <div className="document-upload">
      <div className="document-upload__row">
        <input type="file" ref={fileInputRef} accept=".pdf,.txt,.docx" disabled={uploading} />
        <button onClick={handleUpload} disabled={uploading}>
          {uploading ? "Uploading..." : "Upload"}
        </button>
      </div>

      {status && <div className="document-upload__status">{status}</div>}
    </div>
  );
}