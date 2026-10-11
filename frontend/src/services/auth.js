import api from "./api";
const TOKEN_KEY = "auth_token";
export const auth = {
  async login(email, password) {
    const { data } = await api.post("/auth/login", { email, password });
    auth.setToken(data.token);
    return { email: data.email, fullName: data.fullName, role: data.role };
  },
  async register(fullName, email, password) {
    const { data } = await api.post("/auth/register", { fullName, email, password });
    auth.setToken(data.token);
    return { email: data.email, fullName: data.fullName, role: data.role };
  },
  async me() { return (await api.get("/auth/me")).data; },
  logout() { localStorage.removeItem(TOKEN_KEY); },
  setToken(token) { localStorage.setItem(TOKEN_KEY, token); },
  getToken() { return localStorage.getItem(TOKEN_KEY); },
  isAuthenticated() { return Boolean(auth.getToken()); },
};
export default auth;
