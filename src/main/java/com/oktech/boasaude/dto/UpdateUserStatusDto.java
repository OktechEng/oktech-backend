package com.oktech.boasaude.dto;

import com.oktech.boasaude.entity.UserStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateUserStatusDto(
    @NotNull UserStatus status
) {}
