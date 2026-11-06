import React from "react";
import { Link, NavLink, Outlet, useNavigate } from "react-router-dom";
import useAuth from "../hooks/useAuth";

export default function MainLayout() {
  const {user,logout} = useAuth();
  const navigate = useNavigate();
  function signOut(){logout();navigate("/",{replace:true});}
  return <div className="app-shell">
    <header className="app-header">
      <Link className="brand" to="/">RAG Educational System</Link>
      <nav aria-label="Main navigation">
        {user && <>
          <NavLink to="/chat">Chat</NavLink>
          <NavLink to="/documents">Documents</NavLink>
          <NavLink to="/dashboard">Dashboard</NavLink>
          <span className="nav-user">{user.fullName || user.email}</span>
          <button type="button" onClick={signOut}>Logout</button>
        </>}
        {!user && <NavLink to="/login">Login</NavLink>}
      </nav>
    </header>
    <main><Outlet /></main>
  </div>;
}
