import React from "react";
import { Link } from "react-router-dom";
import "../styles/main.css";

/**
 * Home.jsx — Landing page for the RAG Educational Assistant
 * Route: "/"
 *
 * Design intent:
 * - Theme: a study desk at night — deep ink-navy background, warm
 *   highlighter-amber accent (the moment a highlighter marks a page).
 * - Signature element: a stack of "pages" that visually compress into
 *   a single chat bubble on scroll/hover — the product's whole pitch
 *   (documents → answers) as one animated motif.
 */

const FEATURES = [
  {
    id: "01",
    title: "Upload any material",
    body: "PDFs, lecture notes, and textbooks — dropped in and chunked in seconds.",
  },
  {
    id: "02",
    title: "Ask in plain language",
    body: "Questions are answered from your own documents, not the open web.",
  },
  {
    id: "03",
    title: "See the source",
    body: "Every answer is traced back to the page and passage it came from.",
  },
];

export default function Home() {
  return (
    <div className="home">
      {/* ---------- Hero ---------- */}
      <section className="home-hero">
        <span className="home-hero__eyebrow">Retrieval-Augmented Study Assistant</span>

        <h1 className="home-hero__title">
          Turn your notes into
          <span className="home-hero__highlight"> answers you can trust.</span>
        </h1>

        <p className="home-hero__subtitle">
          Upload your course material once. Ask questions about it forever —
          every answer points back to the exact page it came from.
        </p>

        <div className="home-hero__actions">
          <Link to="/documents" className="btn btn--primary">
            Upload a document
          </Link>
          <Link to="/chat" className="btn btn--ghost">
            Go straight to chat
          </Link>
        </div>

        {/* Signature motif: pages stacking down into a single chat bubble */}
        <div className="home-hero__stack" aria-hidden="true">
          <div className="page page--1" />
          <div className="page page--2" />
          <div className="page page--3" />
          <div className="bubble">
            <span className="bubble__dot" />
            <span className="bubble__dot" />
            <span className="bubble__dot" />
          </div>
        </div>
      </section>

      {/* ---------- Features ---------- */}
      <section className="home-features">
        {FEATURES.map((f) => (
          <div className="feature" key={f.id}>
            <span className="feature__index">{f.id}</span>
            <h3 className="feature__title">{f.title}</h3>
            <p className="feature__body">{f.body}</p>
          </div>
        ))}
      </section>

      {/* ---------- CTA ---------- */}
      <section className="home-cta">
        <h2 className="home-cta__title">Your documents. Your answers.</h2>
        <Link to="/documents" className="btn btn--primary">
          Get started
        </Link>
      </section>
    </div>
  );
}