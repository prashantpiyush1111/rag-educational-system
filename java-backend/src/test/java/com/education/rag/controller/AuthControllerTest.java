package com.education.rag.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.education.rag.dto.request.AuthRequest;
import com.education.rag.dto.response.AuthResponse;
import com.education.rag.repository.UserRepository;
import com.education.rag.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters=false)
class AuthControllerTest {
 @Autowired MockMvc mvc;
 @MockBean AuthService service;
 @MockBean UserRepository users;

 @Test void registerReturnsAuthResponse() throws Exception {
  when(service.register(any(AuthRequest.class))).thenReturn(new AuthResponse("token","student@example.com","Student","STUDENT"));
  mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content("{\"fullName\":\"Student\",\"email\":\"student@example.com\",\"password\":\"password123\"}"))
    .andExpect(status().isOk()).andExpect(jsonPath("$.token").value("token")).andExpect(jsonPath("$.fullName").value("Student"));
 }
 @Test void registerValidatesPasswordLength() throws Exception {
  mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content("{\"fullName\":\"Student\",\"email\":\"student@example.com\",\"password\":\"short\"}"))
    .andExpect(status().isBadRequest()).andExpect(jsonPath("$.detail").exists()).andExpect(jsonPath("$.status").value(400));
 }
 @Test void loginDoesNotRequireFullName() throws Exception {
  when(service.login(any(AuthRequest.class))).thenReturn(new AuthResponse("token","student@example.com","Student","STUDENT"));
  mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"student@example.com\",\"password\":\"password123\"}"))
    .andExpect(status().isOk()).andExpect(jsonPath("$.token").value("token"));
 }
}
