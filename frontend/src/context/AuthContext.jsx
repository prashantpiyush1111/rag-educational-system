import React, { createContext, useState, useEffect, useCallback } from "react";
import auth from "../services/auth";

/**
 * context/AuthContext.js
 * Provides { user, loading, login, register, logout } to the app.
 * Wrap the app (in App.js) with <AuthProvider> so useAuth() works
 * anywhere below it.
 */

export const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // On first load, check if a token already exists (e.g. from a
  // previous session) and treat the user as logged in.
  useEffect(() => {
    if (auth.isAuthenticated()) {
      // NOTE: no "get current user" endpoint exists yet — for now
      // we just mark the session as authenticated without user
      // details. Replace with a real /auth/me call once that
      // endpoint exists on the backend.
      setUser({ authenticated: true });
    }
    setLoading(false);
  }, []);

  const login = useCallback(async (email, password) => {
    const loggedInUser = await auth.login(email, password);
    setUser(loggedInUser);
    return loggedInUser;
  }, []);

  const register = useCallback(async (name, email, password) => {
    return auth.register(name, email, password);
  }, []);

  const logout = useCallback(() => {
    auth.logout();
    setUser(null);
  }, []);

  const value = { user, loading, login, register, logout };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export default AuthContext;