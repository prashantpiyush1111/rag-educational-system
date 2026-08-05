import React, { useState, useEffect } from "react";

const API_BASE =
  import.meta.env.VITE_API_BASE || "http://127.0.0.1:8000";

/**
 * components/Dashboard/MetricsDashboard.jsx
 * Fetches and displays usage/performance metrics from the Python
 * RAG service's GET /metrics/ endpoint.
 *
 * NOTE: assumes a GET /metrics/ endpoint exists on the backend
 * returning something like:
 *   {
 *     documents_count: number,
 *     total_chunks: number,
 *     queries_count: number,
 *     avg_response_time_ms: number
 *   }
 * Adjust field names below if your actual response shape differs.
 */

export default function MetricsDashboard() {
  const [metrics, setMetrics] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    async function fetchMetrics() {
      setLoading(true);
      setError("");
      try {
        const res = await fetch(`${API_BASE}/metrics/`);
        const data = await res.json();

        if (res.ok) {
          setMetrics(data);
        } else {
          setError(data.detail || "Could not load metrics.");
        }
      } catch (err) {
        setError("Could not connect to server. Is it running?");
      } finally {
        setLoading(false);
      }
    }

    fetchMetrics();
  }, []);

  if (loading) return <p className="metrics-dashboard__loading">Loading metrics...</p>;
  if (error) return <p className="metrics-dashboard__error">{error}</p>;

  const cards = [
    { label: "Documents indexed", value: metrics?.documents_count ?? "—" },
    { label: "Total chunks", value: metrics?.total_chunks ?? "—" },
    { label: "Questions answered", value: metrics?.queries_count ?? "—" },
    {
      label: "Avg response time",
      value:
        metrics?.avg_response_time_ms != null
          ? `${metrics.avg_response_time_ms} ms`
          : "—",
    },
  ];

  return (
    <div className="metrics-dashboard">
      {cards.map((card) => (
        <div className="metrics-dashboard__card" key={card.label}>
          <span className="metrics-dashboard__value">{card.value}</span>
          <span className="metrics-dashboard__label">{card.label}</span>
        </div>
      ))}
    </div>
  );
}