import React, { createContext, useState, useEffect, useCallback } from "react";
import auth from "../services/auth";

export const AuthContext = createContext(null);
export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    let active = true;
    const handleLogout = () => { if (active) setUser(null); };
    window.addEventListener("auth:logout", handleLogout);
    (async () => {
      if (auth.isAuthenticated()) {
        try { const current = await auth.me(); if (active) setUser(current); }
        catch { auth.logout(); if (active) setUser(null); }
      }
      if (active) setLoading(false);
    })();
    return () => { active = false; window.removeEventListener("auth:logout", handleLogout); };
  }, []);
  const login = useCallback(async (email, password) => {
    const result = await auth.login(email, password); setUser(result); return result;
  }, []);
  const register = useCallback(async (fullName, email, password) => {
    const result = await auth.register(fullName, email, password); setUser(result); return result;
  }, []);
  const logout = useCallback(() => { auth.logout(); setUser(null); }, []);
  return <AuthContext.Provider value={{user,loading,login,register,logout}}>{children}</AuthContext.Provider>;
}
export default AuthContext;
