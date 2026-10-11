package com.education.rag;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties={"app.jwt.secret=test-secret-key-must-be-at-least-32-bytes-long","app.python-service.api-key=test-only-key"})
class RagApplicationTest {
 @Test void contextLoads() {}
}
