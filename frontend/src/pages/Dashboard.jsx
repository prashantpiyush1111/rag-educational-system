import React from "react";
import MetricsDashboard from "../components/Dashboard/MetricsDashboard";

/**
 * Dashboard.jsx — Route: "/dashboard"
 * Page shell showing system/usage metrics. Actual charts and data
 * fetching live in components/Dashboard/MetricsDashboard.jsx.
 */

export default function Dashboard() {
  return (
    <div className="dashboard-page">
      <header className="dashboard-page__header">
        <h1 className="dashboard-page__title">Dashboard</h1>
        <p className="dashboard-page__subtitle">
          Usage and performance at a glance — documents indexed, queries
          answered, and response times.
        </p>
      </header>

      <MetricsDashboard />
    </div>
  );
}