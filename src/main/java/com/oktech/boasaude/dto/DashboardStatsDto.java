package com.oktech.boasaude.dto;

import java.math.BigDecimal;

// Usando um Record para um DTO simples e imutável
public record DashboardStatsDto(
    long newUsersToday, // novo usuarios hoje
    long totalActiveConsumers, // consumidores ativos
    long totalActiveProducers, // produtores ativos
    BigDecimal revenueToday, // receita hoje
    BigDecimal revenueThisMonth, // receita este mes
    long activeSubscriptions, // assinaturas ativas
    long pendingProductsForApproval // produtos pendentes de aprovacao
) {}