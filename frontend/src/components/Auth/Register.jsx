import React,{useState} from "react";
import {Link,useNavigate} from "react-router-dom";
import useAuth from "../../hooks/useAuth";
import {responseDetail} from "../../services/api";
export default function Register(){
 const {register}=useAuth();const navigate=useNavigate();
 const [fullName,setFullName]=useState("");const [email,setEmail]=useState("");const [password,setPassword]=useState("");const [error,setError]=useState("");const [loading,setLoading]=useState(false);
 async function submit(event){event.preventDefault();setError("");setLoading(true);try{await register(fullName,email,password);navigate("/chat",{replace:true});}catch(err){setError(responseDetail(err,"Unable to create account"));}finally{setLoading(false);}}
 return <section className="auth-card"><h1>Create account</h1><form onSubmit={submit}>
 <label>Full name<input autoComplete="name" value={fullName} onChange={e=>setFullName(e.target.value)} required maxLength={255}/></label>
 <label>Email<input type="email" autoComplete="email" value={email} onChange={e=>setEmail(e.target.value)} required/></label>
 <label>Password<input type="password" autoComplete="new-password" minLength={8} value={password} onChange={e=>setPassword(e.target.value)} required/></label>
 {error&&<p role="alert">{error}</p>}<button type="submit" disabled={loading}>{loading?"Creating account…":"Register"}</button></form>
 <p>Already registered? <Link to="/login">Login</Link></p></section>;
}
