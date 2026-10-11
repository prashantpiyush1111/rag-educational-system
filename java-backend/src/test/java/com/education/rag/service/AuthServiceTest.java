package com.education.rag.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;
import com.education.rag.dto.request.AuthRequest;
import com.education.rag.entity.User;
import com.education.rag.repository.UserRepository;
import com.education.rag.security.jwt.JwtUtil;
import com.education.rag.exception.CustomExceptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
 @Mock UserRepository users; @Mock PasswordEncoder encoder; @Mock JwtUtil jwt;
 @InjectMocks AuthService service;
 AuthRequest request;
 @BeforeEach void setup(){request=new AuthRequest();request.setFullName("Student One");request.setEmail("student@example.com");request.setPassword("strongpass");}
 @Test void registerCreatesStudentAndToken(){
  when(users.existsByEmail("student@example.com")).thenReturn(false);when(encoder.encode("strongpass")).thenReturn("hash");
  when(users.save(any(User.class))).thenAnswer(inv->{User u=inv.getArgument(0);u.setId(1L);return u;});
  when(jwt.generateToken("student@example.com","STUDENT")).thenReturn("signed");
  var result=service.register(request);
  assertEquals("signed",result.token());assertEquals("Student One",result.fullName());
  verify(users).save(argThat(u->u.getRole()==User.Role.STUDENT&&u.getFullName().equals("Student One")));
 }
 @Test void registerRejectsDuplicateEmail(){
  when(users.existsByEmail("student@example.com")).thenReturn(true);
  assertThrows(CustomExceptions.UserAlreadyExistsException.class,()->service.register(request));
  verify(users,never()).save(any());
 }
 @Test void loginRejectsUnknownEmail(){
  when(users.findByEmail("student@example.com")).thenReturn(Optional.empty());
  assertThrows(CustomExceptions.InvalidCredentialsException.class,()->service.login(request));
 }
}