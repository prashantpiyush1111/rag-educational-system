import React from "react";
import { describe,it,expect,vi } from "vitest";
import { render,screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import LoginPage from "./Login";
vi.mock("../hooks/useAuth",()=>({default:()=>({login:vi.fn().mockResolvedValue({email:"student@example.com"})})}));
describe("Login page",()=>{it("renders accessible email and password fields",()=>{render(<MemoryRouter><LoginPage/></MemoryRouter>);expect(screen.getByLabelText(/email/i)).toBeInTheDocument();expect(screen.getByLabelText(/password/i)).toBeInTheDocument();});});
