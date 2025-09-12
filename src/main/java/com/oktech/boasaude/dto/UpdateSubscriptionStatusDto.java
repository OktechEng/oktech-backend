package com.oktech.boasaude.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateSubscriptionStatusDto(
    @NotNull
    Boolean active
) {}