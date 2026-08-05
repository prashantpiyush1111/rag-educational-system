import { useContext } from "react";
import { AuthContext } from "../context/AuthContext";

/**
 * hooks/useAuth.js
 * Thin hook wrapping AuthContext so components can do
 * `const { user, login, register, logout } = useAuth();`
 * instead of importing useContext + AuthContext directly everywhere.
 */

export function useAuth() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }

  return context;
}

export default useAuth;