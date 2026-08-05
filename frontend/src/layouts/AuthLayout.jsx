import React from "react";
import { Outlet, Link } from "react-router-dom";

/**
 * layouts/AuthLayout.jsx
 * Minimal shell for auth pages (Login, Register). No main nav —
 * keeps focus on the form. Renders the active page via <Outlet />.
 * Used by AppRoutes.jsx as a wrapper route for "/login", "/register".
 */

export default function AuthLayout() {
  return (
    <div className="auth-layout">
      <div className="auth-layout__panel">
        <Link to="/" className="auth-layout__brand">
          RAG Educational Assistant
        </Link>

        <div className="auth-layout__content">
          <Outlet />
        </div>

        <p className="auth-layout__back">
          <Link to="/">← Back to home</Link>
        </p>
      </div>
    </div>
  );
}