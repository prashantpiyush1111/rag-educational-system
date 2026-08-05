import React from "react";
import Login from "../components/Auth/Login";

/**
 * pages/Login.jsx — Route: "/login"
 * Page shell for authentication. Actual form fields, validation and
 * the auth API call live in components/Auth/Login.jsx.
 */

export default function LoginPage() {
  return (
    <div className="login-page">
      <header className="login-page__header">
        <h1 className="login-page__title">Welcome back</h1>
        <p className="login-page__subtitle">
          Log in to access your uploaded documents and chat history.
        </p>
      </header>

      <Login />
    </div>
  );
}