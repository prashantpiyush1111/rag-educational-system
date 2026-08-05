import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";

import MainLayout from "../layouts/MainLayout";
import AuthLayout from "../layouts/AuthLayout";

import Home from "../pages/Home";
import Chat from "../pages/Chat";
import Documents from "../pages/Documents";
import Dashboard from "../pages/Dashboard";
import LoginPage from "../pages/Login";

/**
 * routes/AppRoutes.jsx
 * Central route table. Main app pages render inside MainLayout
 * (top nav); auth pages render inside AuthLayout (minimal shell).
 */

export default function AppRoutes() {
  return (
    <Routes>
      <Route element={<MainLayout />}>
        <Route path="/" element={<Home />} />
        <Route path="/chat" element={<Chat />} />
        <Route path="/documents" element={<Documents />} />
        <Route path="/dashboard" element={<Dashboard />} />
      </Route>

      <Route element={<AuthLayout />}>
        <Route path="/login" element={<LoginPage />} />
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}