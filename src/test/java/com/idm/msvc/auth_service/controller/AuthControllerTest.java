package com.idm.msvc.auth_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idm.msvc.auth_service.dto.request.LoginRequest;
import com.idm.msvc.auth_service.dto.request.RegisterRequest;
import com.idm.msvc.auth_service.dto.response.LoginResponse;
import com.idm.msvc.auth_service.dto.response.RegisterResponse;
import com.idm.msvc.auth_service.enums.Rol;
import com.idm.msvc.auth_service.security.JwtService;
import com.idm.msvc.auth_service.security.SecurityConfig;
import com.idm.msvc.auth_service.service.AuthService;
import com.idm.msvc.auth_service.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private AuthenticationService authenticationService;

    @Test
    void login_endpoint_returnsToken() throws Exception {
        LoginRequest req = new LoginRequest("user1", "Password1");
        when(authService.login(req)).thenReturn(new LoginResponse("tok-1"));

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("tok-1"));

        verify(authService).login(req);
    }

    @Test
    void register_endpoint_returnsCreated() throws Exception {
        RegisterRequest req = new RegisterRequest("newuser", "Password1", Rol.CLIENTE);
        when(authService.register(req, Rol.CLIENTE)).thenReturn(new RegisterResponse("newuser"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("newuser"));

        verify(authService).register(req, Rol.CLIENTE);
    }

    @Test
    void register_withNullRol_assignsDefaultAdmin() throws Exception {
        RegisterRequest req = new RegisterRequest("anthony_dev", "Password123", null);

        when(authService.register(any(), any())).thenReturn(new RegisterResponse("anthony_dev"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        verify(authService).register(any(), eq(Rol.ADMIN));
    }

    @Test
    void getUsers_returnsList() throws Exception {
        when(authService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/auth/users"))
                .andExpect(status().isOk());

        verify(authService).getAllUsers();
    }

    @Test
    void validateToken_withValidToken_returnsOk() throws Exception {
        String token = "Bearer valid-token";
        when(jwtService.validateToken("valid-token")).thenReturn("user1");

        mockMvc.perform(get("/auth/validate-token")
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void validateToken_withInvalidToken_returnsUnauthorized() throws Exception {
        String token = "Bearer invalid-token";


        doThrow(new RuntimeException("Token expired"))
                .when(jwtService).validateToken("invalid-token");

        mockMvc.perform(get("/auth/validate-token")
                        .header("Authorization", token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void validateToken_withoutBearer_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/auth/validate-token")
                        .header("Authorization", "InvalidFormat 123"))
                .andExpect(status().isUnauthorized());
    }

}

