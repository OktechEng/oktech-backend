package com.oktech.boasaude.service;

import com.oktech.boasaude.dto.AdminUserViewDto;
import com.oktech.boasaude.dto.CreateSubscriptionDto;
import com.oktech.boasaude.dto.DashboardStatsDto;
import com.oktech.boasaude.dto.SubscriptionViewDto;
import com.oktech.boasaude.entity.UserStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    Page<AdminUserViewDto> getAllUsers(Pageable pageable);
    AdminUserViewDto getUserDetails(UUID userId);
    AdminUserViewDto updateUserStatus(UUID userId, UserStatus newStatus);
    void deleteUser(UUID userId); // Manter a exclusão, que agora fará o soft delete para DELETED
    DashboardStatsDto getDashboardStatistics(); // Novo método para obter estatísticas do dashboard

    // Métodos de Gerenciamento de Assinaturas
    Page<SubscriptionViewDto> getAllSubscriptions(Pageable pageable);
    SubscriptionViewDto getSubscriptionDetails(UUID subscriptionId);
    SubscriptionViewDto createSubscription(CreateSubscriptionDto createDto);
    SubscriptionViewDto updateSubscriptionStatus(UUID subscriptionId, boolean active);
    void deleteSubscription(UUID subscriptionId);
    Page<SubscriptionViewDto> findSubscriptionsByCustomerName(String customerName, Pageable pageable);
    List<SubscriptionViewDto> findExpiringSubscriptions(int days);
}