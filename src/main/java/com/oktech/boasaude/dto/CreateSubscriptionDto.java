package com.oktech.boasaude.dto;

import java.time.LocalDate;

public record CreateSubscriptionDto(
    String customerName,
    String plan,
    LocalDate startDate,
    LocalDate endDate
) {}