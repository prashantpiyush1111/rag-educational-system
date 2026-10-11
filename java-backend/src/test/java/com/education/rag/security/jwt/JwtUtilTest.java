package com.education.rag.security.jwt;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class JwtUtilTest {
 @Test void tokenContainsEmailAndRoleAndValidates(){
  JwtUtil util=new JwtUtil();
  ReflectionTestUtils.setField(util,"secret","test-secret-key-must-be-at-least-32-bytes-long");
  ReflectionTestUtils.setField(util,"expirationMs",60000L);
  String token=util.generateToken("student@example.com","STUDENT");
  assertEquals("student@example.com",util.extractEmail(token));
  assertEquals("STUDENT",util.extractRole(token));
  assertTrue(util.isTokenValid(token,"student@example.com"));
  assertFalse(util.isTokenValid(token,"other@example.com"));
 }
}
