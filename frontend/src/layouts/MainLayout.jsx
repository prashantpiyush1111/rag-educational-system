import React from "react";
import { Outlet, Link, useLocation } from "react-router-dom";

/**
 * layouts/MainLayout.jsx
 * Shared shell for authenticated / main app pages: Home, Chat,
 * Documents, Dashboard. Renders a top nav + the active page via
 * <Outlet />. Used by AppRoutes.jsx as a wrapper route.
 */

const NAV_LINKS = [
  { to: "/", label: "Home" },
  { to: "/chat", label: "Chat" },
  { to: "/documents", label: "Documents" },
  { to: "/dashboard", label: "Dashboard" },
];

export default function MainLayout() {
  const location = useLocation();

  return (
    <div className="main-layout">
      <header className="main-layout__header">
        <Link to="/" className="main-layout__brand">
          RAG Educational Assistant
        </Link>

        <nav className="main-layout__nav">
          {NAV_LINKS.map((link) => (
            <Link
              key={link.to}
              to={link.to}
              className={
                "main-layout__nav-link" +
                (location.pathname === link.to ? " is-active" : "")
              }
            >
              {link.label}
            </Link>
          ))}
        </nav>

        <Link to="/login" className="main-layout__login-btn">
          Log in
        </Link>
      </header>

      <main className="main-layout__content">
        <Outlet />
      </main>

      <footer className="main-layout__footer">
        <span>RAG Educational Assistant — Final Year Project</span>
      </footer>
    </div>
  );
}