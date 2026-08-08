import React from "react";
import AppRoutes from "./routes/AppRoutes";
import { AuthProvider } from "./context/AuthContext";

/**
 * App.jsx
 * Root component — wraps the whole app in AuthProvider so
 * useAuth() works everywhere, then renders the route table.
 */
export default function App() {
  return (
    <AuthProvider>
      <AppRoutes />
    </AuthProvider>
  );
}