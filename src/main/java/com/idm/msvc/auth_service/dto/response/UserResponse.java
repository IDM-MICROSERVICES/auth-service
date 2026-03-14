package com.idm.msvc.auth_service.dto.response;

import com.idm.msvc.auth_service.enums.Rol;

public record UserResponse(
        Long id,
        String username,
        Rol rol
) {
}
