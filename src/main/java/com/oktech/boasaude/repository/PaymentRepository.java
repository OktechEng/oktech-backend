package com.oktech.boasaude.repository;

import com.oktech.boasaude.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    /**
     * Calcula a soma total de pagamentos bem-sucedidos a partir de uma data específica.
     * Essencial para o widget "Receita do Dia" do Dashboard.
     * Usa COALESCE para retornar 0 em vez de NULL se não houver pagamentos.
     * @param date A data/hora de início (ex: o início do dia de hoje).
     * @return A soma total como BigDecimal.
     */
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = 'SUCCESS' AND p.createdAt >= :date")
    BigDecimal sumSuccessfulPaymentsAfter(@Param("date") LocalDateTime date);

    /**
     * Calcula a soma total de pagamentos bem-sucedidos dentro de um intervalo de datas.
     * Essencial para o widget "Receita do Mês" do Dashboard.
     * @param startDate A data/hora de início do intervalo.
     * @param endDate A data/hora de fim do intervalo.
     * @return A soma total como BigDecimal.
     */
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = 'SUCCESS' AND p.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal sumSuccessfulPaymentsBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}