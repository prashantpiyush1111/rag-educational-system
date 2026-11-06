import React from "react";
import { describe,it,expect,vi } from "vitest";
import { render,screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import ChatInterface from "./ChatInterface";
import api from "../../services/api";
vi.mock("../../services/api",()=>({default:{post:vi.fn().mockResolvedValue({data:{answer:"Photosynthesis uses light.",sources:["biology.pdf"],question:"What is photosynthesis?",queryLogId:9}})},responseDetail:(e)=>e?.message||"Request failed"}));
vi.mock("../../services/feedback",()=>({sendFeedback:vi.fn()}));
describe("ChatInterface",()=>{it("submits a question with a stable chat session id and shows answer and sources",async()=>{
 const randomUUID=vi.fn(()=> "test-session-id");
 vi.stubGlobal("crypto",{randomUUID});
 const user=userEvent.setup();render(<ChatInterface/>);
 await user.type(screen.getByRole("textbox",{name:/question/i}),"What is photosynthesis?");
 await user.click(screen.getByRole("button",{name:"Ask"}));
 expect(await screen.findByText("Photosynthesis uses light.")).toBeInTheDocument();
 expect(screen.getByText("biology.pdf")).toBeInTheDocument();
 expect(api.post).toHaveBeenCalledWith("/query",{question:"What is photosynthesis?",topK:4,sessionId:"test-session-id"});
 expect(randomUUID).toHaveBeenCalledOnce();
 vi.unstubAllGlobals();

 it("creates a fallback session id when randomUUID is unavailable",async()=>{
  vi.stubGlobal("crypto",{});
  const user=userEvent.setup();render(<ChatInterface/>);
  await user.type(screen.getByRole("textbox",{name:/question/i}),"What is photosynthesis?");
  await user.click(screen.getByRole("button",{name:"Ask"}));
  expect(await screen.findByText("Photosynthesis uses light.")).toBeInTheDocument();
  const payload=api.post.mock.calls.at(-1)[1];
  expect(payload.sessionId).toMatch(/^session-\\d+-[a-z0-9]+$/);
  vi.unstubAllGlobals();
 });
});
