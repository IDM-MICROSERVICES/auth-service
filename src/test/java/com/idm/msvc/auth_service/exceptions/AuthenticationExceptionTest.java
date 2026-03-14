package com.idm.msvc.auth_service.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationExceptionTest {

    @Test
    void testAuthenticationExceptionWithMessage() {
        String message = "Credenciales inválidas";
        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            throw new AuthenticationException(message);
        });
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testAuthenticationExceptionIsRuntimeException() {
        AuthenticationException exception = new AuthenticationException("test");
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testAuthenticationExceptionWithDifferentMessages() {
        String[] messages = {
                "Usuario no encontrado",
                "Contraseña incorrecta",
                "Token expirado",
                "Token inválido"
        };

        for (String message : messages) {
            AuthenticationException exception = new AuthenticationException(message);
            assertEquals(message, exception.getMessage());
        }
    }

    @Test
    void testAuthenticationExceptionCanBeCaught() {
        String errorMessage = "Acceso denegado";
        try {
            throw new AuthenticationException(errorMessage);
        } catch (AuthenticationException e) {
            assertEquals(errorMessage, e.getMessage());
        }
    }

    @Test
    void testAuthenticationExceptionWithNullMessage() {
        AuthenticationException exception = new AuthenticationException(null);
        assertNull(exception.getMessage());
    }

    @Test
    void testAuthenticationExceptionWithEmptyMessage() {
        String message = "";
        AuthenticationException exception = new AuthenticationException(message);
        assertEquals(message, exception.getMessage());
    }
}

