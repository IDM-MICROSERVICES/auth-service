package com.idm.msvc.auth_service.controller;

import com.idm.msvc.auth_service.dto.request.LoginRequest;
import com.idm.msvc.auth_service.dto.request.RegisterRequest;
import com.idm.msvc.auth_service.dto.response.LoginResponse;
import com.idm.msvc.auth_service.dto.response.RegisterResponse;
import com.idm.msvc.auth_service.dto.response.UserResponse;
import com.idm.msvc.auth_service.enums.Rol;
import com.idm.msvc.auth_service.security.JwtService;
import com.idm.msvc.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;
    private final JwtService jwtService;

    public AuthController(AuthService service, JwtService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest request){

        return ResponseEntity.ok(service.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request
            ){
        Rol rolAsignado = Rol.ADMIN;

        if (request.rol() != null) {
            try {
                rolAsignado = Rol.valueOf(request.rol().toString().toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Rol inválido recibido: {}. Se asigna ADMIN por defecto.", request.rol());
            }
        }

        RegisterResponse response = service.register(request,rolAsignado);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String token = authHeader.substring(7);
            jwtService.validateToken(token);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
