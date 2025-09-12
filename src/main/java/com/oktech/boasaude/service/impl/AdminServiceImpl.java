package com.oktech.boasaude.service.impl;

import com.oktech.boasaude.dto.AdminUserViewDto;
import com.oktech.boasaude.dto.CreateSubscriptionDto;
import com.oktech.boasaude.dto.DashboardStatsDto;
import com.oktech.boasaude.dto.SubscriptionViewDto;
import com.oktech.boasaude.entity.Subscription;
import com.oktech.boasaude.entity.User;
import com.oktech.boasaude.entity.UserRole;
import com.oktech.boasaude.entity.UserStatus;
import com.oktech.boasaude.repository.PaymentRepository;
import com.oktech.boasaude.repository.ProductRepository;
import com.oktech.boasaude.repository.SubscriptionRepository;
import com.oktech.boasaude.repository.UserRepository;
import com.oktech.boasaude.service.AdminService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageImpl;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;

    // Converter para DTO
    private SubscriptionViewDto toDto(Subscription subscription) {
        return new SubscriptionViewDto(
            subscription.getId(),
            subscription.getCustomerName(),
            subscription.getPlan(),
            subscription.getStartDate(),
            subscription.getEndDate(),
            subscription.getIsActive()
        );
    }

    @Override
    public Page<SubscriptionViewDto> getAllSubscriptions(Pageable pageable) {
        return subscriptionRepository.findAll(pageable)
            .map(this::toDto);
    }

    @Override
    public SubscriptionViewDto getSubscriptionDetails(UUID subscriptionId) {
        return subscriptionRepository.findById(subscriptionId)
            .map(this::toDto)
            .orElseThrow(() -> new EntityNotFoundException("Subscription not found"));
    }

    @Override
    public SubscriptionViewDto createSubscription(CreateSubscriptionDto createDto) {
        Subscription subscription = new Subscription(
            createDto.customerName(),
            createDto.plan(),
            createDto.startDate(),
            true
        );
        subscription.setEndDate(createDto.endDate());
        
        return toDto(subscriptionRepository.save(subscription));
    }

    @Override
    public SubscriptionViewDto updateSubscriptionStatus(UUID subscriptionId, boolean active) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
            .orElseThrow(() -> new EntityNotFoundException("Subscription not found"));
            
        subscription.setIsActive(active);
        return toDto(subscriptionRepository.save(subscription));
    }

    @Override
    public void deleteSubscription(UUID subscriptionId) {
        subscriptionRepository.deleteById(subscriptionId);
    }

    @Override
    public Page<SubscriptionViewDto> findSubscriptionsByCustomerName(String customerName, Pageable pageable) {
        return subscriptionRepository.findByCustomerNameContainingIgnoreCase(customerName)
            .stream()
            .map(this::toDto)
            .collect(Collectors.toCollection(() ->
                new PageImpl<>(new ArrayList<>(), pageable, 0)
            ));
    }

    @Override
    public List<SubscriptionViewDto> findExpiringSubscriptions(int days) {
        return subscriptionRepository.findSubscriptionsExpiringBetween(
            LocalDate.now(),
            LocalDate.now().plusDays(days)
        ).stream()
        .map(this::toDto)
        .collect(Collectors.toList());
    }


    @Override
    public DashboardStatsDto getDashboardStatistics() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime startOfMonth = YearMonth.now().atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = YearMonth.now().atEndOfMonth().atTime(23, 59, 59);
        // 1. Coleta dados de Usuários
        long newUsers = userRepository.countByCreatedAtAfter(startOfDay);
        long consumers = userRepository.countByRoleAndStatus(UserRole.USER, UserStatus.ACTIVE);
        long producers = userRepository.countByRoleAndStatus(UserRole.PRODUCTOR, UserStatus.ACTIVE);

        // 2. Coleta dados Financeiros (pode precisar de queries customizadas)
        BigDecimal revToday = paymentRepository.sumSuccessfulPaymentsAfter(startOfDay);
        BigDecimal revMonth = paymentRepository.sumSuccessfulPaymentsThisMonth();

        // 3. Coleta dados de Operações
        long activeSubs = subscriptionRepository.countByStatus(SubscriptionStatus.ACTIVE);

        // 4. Coleta dados de Tarefas Administrativas
        long pendingProducts = productRepository.countByStatus(ProductStatus.PENDING_APPROVAL);

        // 5. Monta e retorna o DTO
        return new DashboardStatsDto(newUsers, consumers, producers, revToday, revMonth, activeSubs, pendingProducts);
    }

    @Override
    public Page<AdminUserViewDto> getAllUsers(Pageable pageable) {
        // O @Where na entidade já filtra os usuários 'DELETED'
        return userRepository.findAll(pageable).map(AdminUserViewDto::new);
    }

    @Override
    public AdminUserViewDto getUserDetails(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return new AdminUserViewDto(user);
    }

    @Override
    public AdminUserViewDto updateUserStatus(UUID userId, UserStatus newStatus) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // Regra de negócio: não permitir que um admin delete a si mesmo via status
        // Adicionar mais regras conforme necessário (ex: não suspender outros admins)
        if (newStatus == UserStatus.DELETED) {
            throw new IllegalArgumentException("Use the DELETE endpoint to delete a user.");
        }

        user.setStatus(newStatus);
        User updatedUser = userRepository.save(user);
        return new AdminUserViewDto(updatedUser);
    }

    @Override
    public void deleteUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        // O @SQLDelete na entidade fará o soft delete para o status 'DELETED'
        userRepository.deleteById(userId);
    }
}