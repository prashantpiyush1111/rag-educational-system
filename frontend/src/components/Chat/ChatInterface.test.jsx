import React from "react";
import { describe,it,expect,vi } from "vitest";
import { render,screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import ChatInterface from "./ChatInterface";
vi.mock("../../services/api",()=>({default:{post:vi.fn().mockResolvedValue({data:{answer:"Photosynthesis uses light.",sources:["biology.pdf"],question:"What is photosynthesis?"}})},responseDetail:(e)=>e?.message||"Request failed"}));
vi.mock("../../services/feedback",()=>({sendFeedback:vi.fn()}));
describe("ChatInterface",()=>{it("submits questions and renders the answer with sources",async()=>{const user=userEvent.setup();render(<ChatInterface/>);await user.type(screen.getByRole("textbox",{name:/question/i}),"What is photosynthesis?");await user.click(screen.getByRole("button",{name:"Ask"}));expect(await screen.findByText("Photosynthesis uses light.")).toBeInTheDocument();expect(screen.getByText("biology.pdf")).toBeInTheDocument();});});
