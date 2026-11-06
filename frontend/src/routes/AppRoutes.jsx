import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import MainLayout from "../layouts/MainLayout";
import AuthLayout from "../layouts/AuthLayout";
import Home from "../pages/Home";
import Chat from "../pages/Chat";
import Documents from "../pages/Documents";
import Dashboard from "../pages/Dashboard";
import LoginPage from "../pages/Login";
import RegisterPage from "../pages/Register";
import ProtectedRoute from "./ProtectedRoute";

const protectedPage = (element) => <ProtectedRoute>{element}</ProtectedRoute>;
export default function AppRoutes() {
  return <Routes>
    <Route element={<MainLayout />}>
      <Route path="/" element={<Home />} />
      <Route path="/chat" element={protectedPage(<Chat />)} />
      <Route path="/documents" element={protectedPage(<Documents />)} />
      <Route path="/dashboard" element={protectedPage(<Dashboard />)} />
    </Route>
    <Route element={<AuthLayout />}>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
    </Route>
    <Route path="*" element={<Navigate to="/" replace />} />
  </Routes>;
}
