import React, { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import useAuth from "../../hooks/useAuth";
import { responseDetail } from "../../services/api";

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [email,setEmail]=useState("");
  const [password,setPassword]=useState("");
  const [error,setError]=useState("");
  const [loading,setLoading]=useState(false);
  async function submit(event){event.preventDefault();setError("");setLoading(true);try{await login(email,password);navigate(location.state?.from?.pathname||"/chat",{replace:true});}catch(err){setError(responseDetail(err,"Invalid email or password"));}finally{setLoading(false);}}
  return <section className="auth-card"><h1>Login</h1><form onSubmit={submit}>
    <label>Email<input type="email" autoComplete="email" value={email} onChange={e=>setEmail(e.target.value)} required/></label>
    <label>Password<input type="password" autoComplete="current-password" value={password} onChange={e=>setPassword(e.target.value)} required/></label>
    {error&&<p role="alert">{error}</p>}<button type="submit" disabled={loading}>{loading?"Signing in…":"Login"}</button>
  </form><p>New here? <Link to="/register">Create an account</Link></p></section>;
}
