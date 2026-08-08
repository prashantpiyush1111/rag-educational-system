/**
 * utils/constants.js
 * Central place for app-wide constants — API base URLs, storage
 * keys, and shared enums. Import from here instead of hardcoding
 * strings across services/components.
 */

export const PYTHON_API_BASE =
  import.meta.env.VITE_API_BASE || "http://127.0.0.1:8000";

export const JAVA_API_BASE =
  import.meta.env.VITE_JAVA_API_BASE || "http://127.0.0.1:9090";

export const TOKEN_KEY = "auth_token";

export const USER_ROLES = {
  STUDENT: "STUDENT",
  ADMIN: "ADMIN",
};

export const ROUTES = {
  HOME: "/",
  CHAT: "/chat",
  DOCUMENTS: "/documents",
  DASHBOARD: "/dashboard",
  LOGIN: "/login",
};