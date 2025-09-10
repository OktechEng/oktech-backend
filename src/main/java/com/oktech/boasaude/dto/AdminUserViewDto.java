package com.oktech.boasaude.dto;

import com.oktech.boasaude.entity.User;
import com.oktech.boasaude.entity.UserRole;
import com.oktech.boasaude.entity.UserStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record AdminUserViewDto(
    UUID id,
    String name,
    String email,
    UserRole role,
    UserStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public AdminUserViewDto(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getStatus(), user.getCreatedAt(), user.getUpdatedAt());
    }
}