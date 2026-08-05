/**
 * services/auth.js
 * Handles auth API calls and token storage. Used by hooks/useAuth.js.
 *
 * NOTE: assumes backend auth endpoints exist at:
 *   POST /auth/login    { email, password } -> { token, user }
 *   POST /auth/register { name, email, password } -> { token, user }
 * Adjust paths/fields below if your actual Java backend routes differ.
 */

const API_BASE = process.env.REACT_APP_API_BASE || "http://127.0.0.1:8000";
const TOKEN_KEY = "auth_token";

export const auth = {
  async login(email, password) {
    const res = await fetch(`${API_BASE}/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password }),
    });
    const data = await res.json();

    if (!res.ok) {
      throw new Error(data?.detail || "Invalid email or password.");
    }

    auth.setToken(data.token);
    return data.user;
  },

  async register(name, email, password) {
    const res = await fetch(`${API_BASE}/auth/register`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name, email, password }),
    });
    const data = await res.json();

    if (!res.ok) {
      throw new Error(data?.detail || "Could not create account.");
    }

    return data.user;
  },

  logout() {
    localStorage.removeItem(TOKEN_KEY);
  },

  setToken(token) {
    localStorage.setItem(TOKEN_KEY, token);
  },

  getToken() {
    return localStorage.getItem(TOKEN_KEY);
  },

  isAuthenticated() {
    return !!auth.getToken();
  },
};

export default auth;