package com.idm.msvc.auth_service.service;

import com.idm.msvc.auth_service.entity.User;
import com.idm.msvc.auth_service.repository.UserRepository;
import com.idm.msvc.auth_service.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {
    private final UserRepository repository;

    public AuthenticationService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El Usuario no está registrado"));

        return new CustomUserDetails(user);
    }


}
