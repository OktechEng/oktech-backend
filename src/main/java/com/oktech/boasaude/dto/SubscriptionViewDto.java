package com.oktech.boasaude.dto;

import java.time.LocalDate;
import java.util.UUID;

public record SubscriptionViewDto(
    UUID id,
    String customerName,
    String plan,
    LocalDate startDate,
    LocalDate endDate,
    Boolean isActive
) {}