import React from "react";
import DocumentUpload from "../components/Documents/DocumentUpload";
import DocumentList from "../components/Documents/DocumentList";

/**
 * Documents.jsx — Route: "/documents"
 * Page shell for managing uploaded documents. Upload logic lives in
 * components/Documents/DocumentUpload.jsx, listing logic in
 * components/Documents/DocumentList.jsx.
 */

export default function Documents() {
  return (
    <div className="documents-page">
      <header className="documents-page__header">
        <h1 className="documents-page__title">Your documents</h1>
        <p className="documents-page__subtitle">
          Upload material once — ask questions about it anytime from the chat.
        </p>
      </header>

      <DocumentUpload />
      <DocumentList />
    </div>
  );
}