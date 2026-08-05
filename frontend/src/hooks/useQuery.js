import { useState, useCallback } from "react";
import api from "../services/api";

/**
 * hooks/useQuery.js
 * Encapsulates asking a question against the RAG service — loading
 * state, error handling, and the last answer — so any component
 * (not just ChatInterface) can reuse this without duplicating fetch
 * logic.
 */

export function useQuery() {
  const [answer, setAnswer] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const ask = useCallback(async (question, topK = 4) => {
    if (!question?.trim()) return null;

    setLoading(true);
    setError("");

    try {
      const data = await api.askQuestion(question.trim(), topK);
      setAnswer(data);
      return data;
    } catch (err) {
      const message = err?.message || "Something went wrong.";
      setError(message);
      return null;
    } finally {
      setLoading(false);
    }
  }, []);

  const reset = useCallback(() => {
    setAnswer(null);
    setError("");
  }, []);

  return { ask, answer, loading, error, reset };
}

export default useQuery;