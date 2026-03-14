package com.idm.msvc.auth_service.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserAlreadyExistsExceptionTest {

    @Test
    void testUserAlreadyExistsExceptionWithUsername() {
        String username = "testuser";
        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            throw new UserAlreadyExistsException(username);
        });
        String expectedMessage = "El usuario '" + username + "' ya existe";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testUserAlreadyExistsExceptionIsRuntimeException() {
        UserAlreadyExistsException exception = new UserAlreadyExistsException("user");
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testUserAlreadyExistsExceptionWithSpecialCharacters() {
        String username = "user@domain.com";
        UserAlreadyExistsException exception = new UserAlreadyExistsException(username);
        String expectedMessage = "El usuario '" + username + "' ya existe";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testUserAlreadyExistsExceptionCanBeCaught() {
        String testUsername = "admin";
        try {
            throw new UserAlreadyExistsException(testUsername);
        } catch (UserAlreadyExistsException e) {
            assertEquals("El usuario '" + testUsername + "' ya existe", e.getMessage());
        }
    }
}

