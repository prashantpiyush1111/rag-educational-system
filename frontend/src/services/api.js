import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE || "/api",
  timeout: 30000,
  headers: { "Content-Type": "application/json" },
});

const TOKEN_KEY = "auth_token";
api.interceptors.request.use((config) => {
  const token = localStorage.getItem(TOKEN_KEY);
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem(TOKEN_KEY);
      window.dispatchEvent(new CustomEvent("auth:logout"));
    }
    return Promise.reject(error);
  }
);
export function responseDetail(error, fallback = "Request failed") {
  return error?.response?.data?.detail || error?.message || fallback;
}
export default api;
