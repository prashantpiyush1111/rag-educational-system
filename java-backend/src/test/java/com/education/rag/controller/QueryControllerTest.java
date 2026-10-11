package com.education.rag.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.education.rag.dto.request.QueryRequest;
import com.education.rag.dto.response.QueryResponse;
import com.education.rag.service.QueryService;
import com.education.rag.exception.GlobalExceptionHandler;
import com.education.rag.security.jwt.JwtUtil;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(QueryController.class)
@AutoConfigureMockMvc(addFilters=false)
@Import(GlobalExceptionHandler.class)
class QueryControllerTest {
 @Autowired MockMvc mvc;
 @MockBean QueryService queryService;
 @MockBean JwtUtil jwtUtil;

 @Test void returnsQueryResponse() throws Exception {
  when(queryService.query(eq("student@example.com"),any(QueryRequest.class)))
   .thenReturn(new QueryResponse("Grounded answer",List.of("notes.pdf"),"What is RAG?",42L));
  mvc.perform(post("/api/query").principal(()->"student@example.com").contentType(MediaType.APPLICATION_JSON)
    .content("{\"question\":\"What is RAG?\"}"))
   .andExpect(status().isOk()).andExpect(jsonPath("$.answer").value("Grounded answer"))
   .andExpect(jsonPath("$.sources[0]").value("notes.pdf"));
 }
 @Test void validatesEmptyQuestion() throws Exception {
  mvc.perform(post("/api/query").principal(()->"student@example.com").contentType(MediaType.APPLICATION_JSON)
    .content("{\"question\":\"\"}"))
   .andExpect(status().isBadRequest()).andExpect(jsonPath("$.detail").exists());
 }
}
