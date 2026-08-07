/**
 * services/api.js
 * Central place for calls to the Python RAG service. Other files
 * (hooks, components) should import from here instead of calling
 * fetch() directly, so the base URL and error handling stay in
 * one place.
 */

const API_BASE =
  import.meta.env.VITE_API_BASE || "http://127.0.0.1:8000";
const API_KEY = import.meta.env.VITE_INTERNAL_API_KEY;

async function request(path, options = {}) {
  const res = await fetch(`${API_BASE}${path}`, {
    headers: {
      "Content-Type": "application/json",
      "X-API-Key": API_KEY,
      ...options.headers,
    },
    ...options,
  });

  let data = null;
  try {
    data = await res.json();
  } catch (err) {
    // response had no JSON body
  }

  if (!res.ok) {
    const message = data?.detail || `Request failed (${res.status})`;
    throw new Error(message);
  }

  return data;
}

export const api = {
  // ---- Chat / query ----
  askQuestion(question, topK = 4) {
    return request("/query/", {
      method: "POST",
      body: JSON.stringify({ question, top_k: topK }),
    });
  },

  // ---- Documents ----
  uploadDocument(file) {
    const formData = new FormData();
    formData.append("file", file);

    return fetch(`${API_BASE}/documents/upload`, {
      method: "POST",
      headers: { "X-API-Key": API_KEY },
      body: formData,
    }).then(async (res) => {
      const data = await res.json();
      if (!res.ok) throw new Error(data?.detail || "Upload failed");
      return data;
    });
  },

  listDocuments() {
    return request("/documents/");
  },

  // ---- Metrics ----
  getMetrics() {
    return request("/metrics/");
  },
};

export default api;