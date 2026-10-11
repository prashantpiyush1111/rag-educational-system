package com.education.rag.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.education.rag.dto.request.QueryRequest;
import com.education.rag.dto.response.QueryResponse;
import com.education.rag.service.QueryService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(QueryController.class)
@AutoConfigureMockMvc(addFilters = false)
class QueryControllerTest {
 @Autowired MockMvc mvc; @MockBean QueryService queryService;
 @Test void requiresJwt() throws Exception {
  mvc.perform(post("/api/query").contentType(MediaType.APPLICATION_JSON).content("{\"question\":\"What is RAG?\"}"))
    .andExpect(status().isUnauthorized());
 }
}
