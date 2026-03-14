package com.idm.msvc.auth_service.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void testHandleAuthenticationException() {
        String errorMessage = "Credenciales inválidas";
        AuthenticationException exception = new AuthenticationException(errorMessage);
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleAuthenticationException(exception);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().get("error"));
    }

    @Test
    void testHandleUserAlreadyExistsException() {
        String username = "existinguser";
        UserAlreadyExistsException exception = new UserAlreadyExistsException(username);
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleUserAlreadyExists(exception);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("El usuario '" + username + "' ya existe", response.getBody().get("error"));
    }

    @Test
    void testHandleValidationErrors() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        org.springframework.validation.BindingResult bindingResult = mock(org.springframework.validation.BindingResult.class);

        FieldError fieldError = new FieldError("object", "username", "El campo username es requerido");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationErrors(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("El campo username es requerido", response.getBody().get("username"));
    }

    @Test
    void testHandleValidationErrorsMultipleFields() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        org.springframework.validation.BindingResult bindingResult = mock(org.springframework.validation.BindingResult.class);

        FieldError error1 = new FieldError("object", "username", "El campo username es requerido");
        FieldError error2 = new FieldError("object", "password", "El campo password es requerido");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(error1, error2));

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationErrors(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("El campo username es requerido", response.getBody().get("username"));
        assertEquals("El campo password es requerido", response.getBody().get("password"));
    }

    @Test
    void testHandleValidationErrorsEmptyList() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        org.springframework.validation.BindingResult bindingResult = mock(org.springframework.validation.BindingResult.class);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of());

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationErrors(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
    }
}

