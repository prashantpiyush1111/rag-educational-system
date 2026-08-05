import React, { useState, useEffect, useCallback } from "react";

const API_BASE =
  import.meta.env.VITE_API_BASE || "http://127.0.0.1:8000";

/**
 * components/Documents/DocumentList.jsx
 * Fetches and displays the list of uploaded documents from the
 * Python RAG service's GET /documents/ endpoint.
 *
 * NOTE: assumes a GET /documents/ endpoint exists on the backend
 * returning something like:
 *   [{ id, filename, chunks_created, uploaded_at }, ...]
 * Adjust the fetch URL / field names if your actual route differs.
 */

export default function DocumentList({ refreshKey }) {
  const [documents, setDocuments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const fetchDocuments = useCallback(async () => {
    setLoading(true);
    setError("");
    try {
      const res = await fetch(`${API_BASE}/documents/`);
      const data = await res.json();

      if (res.ok) {
        setDocuments(Array.isArray(data) ? data : data.documents || []);
      } else {
        setError(data.detail || "Could not load documents.");
      }
    } catch (err) {
      setError("Could not connect to server. Is it running?");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchDocuments();
  }, [fetchDocuments, refreshKey]);

  if (loading) return <p className="document-list__loading">Loading documents...</p>;
  if (error) return <p className="document-list__error">{error}</p>;
  if (documents.length === 0) {
    return <p className="document-list__empty">No documents uploaded yet.</p>;
  }

  return (
    <ul className="document-list">
      {documents.map((doc) => (
        <li key={doc.id || doc.filename} className="document-list__item">
          <span className="document-list__name">{doc.filename}</span>
          {doc.chunks_created != null && (
            <span className="document-list__chunks">{doc.chunks_created} chunks</span>
          )}
        </li>
      ))}
    </ul>
  );
}