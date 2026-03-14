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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService service;

    @Test
    void login_successful_returnsToken() {
        LoginRequest req = new LoginRequest("user1", "Password1");
        User user = new User(1L, "user1", "encodedPass", Rol.CLIENTE);

        when(repository.findByUsername("user1")).thenReturn(Optional.of(user));
        when(encoder.matches("Password1", "encodedPass")).thenReturn(true);
        when(jwtService.generateToken("user1")).thenReturn("token-123");

        LoginResponse resp = service.login(req);

        assertNotNull(resp);
        assertEquals("token-123", resp.token());

        verify(repository).findByUsername("user1");
        verify(encoder).matches("Password1", "encodedPass");
        verify(jwtService).generateToken("user1");
    }

    @Test
    void login_invalidCredentials_throwsAuthenticationException() {
        LoginRequest req = new LoginRequest("user2", "badpass");

        when(repository.findByUsername("user2")).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> service.login(req));

        verify(repository).findByUsername("user2");
        verifyNoInteractions(encoder);
        verifyNoInteractions(jwtService);
    }

    @Test
    void register_successful_returnsRegisterResponse() {
        RegisterRequest req = new RegisterRequest("newuser", "Password1", Rol.CLIENTE);

        when(repository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(encoder.encode("Password1")).thenReturn("encodedPass");
        when(repository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(10L);
            return u;
        });

        RegisterResponse resp = service.register(req, Rol.CLIENTE);

        assertNotNull(resp);
        assertEquals("newuser", resp.username());

        verify(repository).findByUsername("newuser");
        verify(encoder).encode("Password1");
        verify(repository).save(any(User.class));
    }

    @Test
    void register_existingUser_throwsUserAlreadyExistsException() {
        RegisterRequest req = new RegisterRequest("existing", "Password1", Rol.CLIENTE);
        User existing = new User(5L, "existing", "pwd", Rol.CLIENTE);

        when(repository.findByUsername("existing")).thenReturn(Optional.of(existing));

        assertThrows(UserAlreadyExistsException.class, () -> service.register(req, Rol.CLIENTE));

        verify(repository).findByUsername("existing");
        verifyNoMoreInteractions(repository);
    }

    @Test
    void getAllUsers_returnsUserResponses() {
        User u1 = new User(1L, "u1", "p1", Rol.CLIENTE);
        User u2 = new User(2L, "u2", "p2", Rol.ADMIN);

        when(repository.findAll()).thenReturn(List.of(u1, u2));

        List<UserResponse> users = service.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("u1", users.get(0).username());
        assertEquals(Rol.CLIENTE, users.get(0).rol());

        verify(repository).findAll();
    }

}

