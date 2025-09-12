package com.oktech.boasaude.repository;

import com.oktech.boasaude.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    // Buscar todas as assinaturas ativas
    List<Subscription> findByIsActiveTrue();
    
    // Buscar assinaturas por nome do cliente (case insensitive)
    List<Subscription> findByCustomerNameContainingIgnoreCase(String name);
    
    // Buscar assinaturas que vencem em uma data específica
    List<Subscription> findByEndDate(LocalDate endDate);
    
    // Buscar assinaturas por plano
    List<Subscription> findByPlan(String plan);
    
    // Contar assinaturas ativas
    long countByIsActiveTrue();
    
    // Buscar assinaturas que vão vencer nos próximos X dias
    @Query("SELECT s FROM Subscription s WHERE s.endDate BETWEEN :start AND :end AND s.isActive = true")
    List<Subscription> findSubscriptionsExpiringBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
