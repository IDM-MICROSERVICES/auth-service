package com.idm.msvc.auth_service.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", "test-secret-key");
        ReflectionTestUtils.setField(jwtService, "expiration", 3600000L);
    }

    @Test
    void generateToken_and_validateToken_success() {
        String token = jwtService.generateToken("alice");
        assertNotNull(token);

        String subject = jwtService.validateToken(token);
        assertEquals("alice", subject);
    }

    @Test
    void validateToken_invalidToken_throwsException() {
        String badToken = "this.is.not.valid";

        assertThrows(Exception.class, () -> jwtService.validateToken(badToken));
    }

}

