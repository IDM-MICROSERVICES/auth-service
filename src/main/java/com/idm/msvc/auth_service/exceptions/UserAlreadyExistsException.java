package com.idm.msvc.auth_service.exceptions;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String username) {
        super("El usuario '" + username + "' ya existe");
    }
}
