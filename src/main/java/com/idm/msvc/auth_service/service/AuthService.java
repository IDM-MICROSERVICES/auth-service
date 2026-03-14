package com.idm.msvc.auth_service.service;

import com.idm.msvc.auth_service.dto.request.LoginRequest;
import com.idm.msvc.auth_service.dto.request.RegisterRequest;
import com.idm.msvc.auth_service.dto.response.LoginResponse;
import com.idm.msvc.auth_service.dto.response.RegisterResponse;
import com.idm.msvc.auth_service.dto.response.UserResponse;
import com.idm.msvc.auth_service.entity.User;
import com.idm.msvc.auth_service.enums.Rol;
import com.idm.msvc.auth_service.exceptions.AuthenticationException;
import com.idm.msvc.auth_service.exceptions.UserAlreadyExistsException;
import com.idm.msvc.auth_service.repository.UserRepository;
import com.idm.msvc.auth_service.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthService(UserRepository repository,
                       PasswordEncoder encoder,
                       JwtService jwtService) {
        this.repository = repository;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request){

        String token = repository.findByUsername(request.username())
                .filter(user -> encoder.matches(request.password(),
                        user.getPassword()))
                .map(user -> jwtService.generateToken(user.getUsername()))
                .orElseThrow(()->new AuthenticationException("Usuario o Contraseña inválidos"));

        return new LoginResponse(token);

    }

    public RegisterResponse register(RegisterRequest request, Rol rol){

        log.info("Rol: "+rol);
        repository.findByUsername(request.username())
                .ifPresent(u -> { throw new UserAlreadyExistsException(request.username()); });

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(encoder.encode(request.password()));

        user.setRole(
                switch (rol){
                    case CLIENTE -> Rol.CLIENTE;
                    case EMPLEADO -> Rol.EMPLEADO;
                    case ADMIN -> Rol.ADMIN;
                }

        );

        User savedUser = repository.save(user);
        log.info("Usuario guardado: " + savedUser.getUsername()+"con rol "+savedUser.getRole());

        return new RegisterResponse(user.getUsername());
    }

    public List<UserResponse> getAllUsers() {
        return repository.findAll()
                .stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername(), user.getRole()))
                .toList();
    }

}
