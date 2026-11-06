import React from "react";
import { describe,it,expect,vi } from "vitest";
import { render,screen } from "@testing-library/react";
import { MemoryRouter,Routes,Route } from "react-router-dom";
import ProtectedRoute from "./ProtectedRoute";
vi.mock("../hooks/useAuth",()=>({default:()=>({user:null,loading:false})}));
describe("ProtectedRoute",()=>{it("redirects unauthenticated users to login",()=>{render(<MemoryRouter initialEntries={["/chat"]}><Routes><Route path="/chat" element={<ProtectedRoute><p>Secret</p></ProtectedRoute>}/><Route path="/login" element={<p>Login page</p>}/></Routes></MemoryRouter>);expect(screen.getByText("Login page")).toBeInTheDocument();expect(screen.queryByText("Secret")).not.toBeInTheDocument();});});
