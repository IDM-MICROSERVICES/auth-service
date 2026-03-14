package com.idm.msvc.auth_service.security;

import com.idm.msvc.auth_service.entity.User;
import com.idm.msvc.auth_service.enums.Rol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerUserDetailsTest {
    private User user;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("anthony_dev");
        user.setPassword("encoded_password");
        user.setRole(Rol.CLIENTE);

        customUserDetails = new CustomUserDetails(user);
    }

    @Test
    void shouldReturnCorrectUsername() {
        assertEquals("anthony_dev", customUserDetails.getUsername());
    }

    @Test
    void shouldReturnCorrectPassword() {
        assertEquals("encoded_password", customUserDetails.getPassword());
    }

    @Test
    void shouldReturnCorrectAuthorities() {
        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());

        assertTrue(authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE")));
    }

    @Test
    void shouldReturnDefaultBooleansAsTrue() {
        assertTrue(customUserDetails.isAccountNonExpired());
        assertTrue(customUserDetails.isAccountNonLocked());
        assertTrue(customUserDetails.isCredentialsNonExpired());
        assertTrue(customUserDetails.isEnabled());
    }
}
