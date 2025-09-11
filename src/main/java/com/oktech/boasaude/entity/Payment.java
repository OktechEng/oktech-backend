package com.oktech.boasaude.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

// Enum para o status do pagamento
enum PaymentStatus {
    PENDING,
    SUCCESS,
    FAILED,
    REFUNDED
}

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Relacionamento com o usuário que fez o pagamento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // NOTA: No futuro, você provavelmente terá um relacionamento com uma entidade 'Order' ou 'Subscription' aqui.

    // Use BigDecimal para valores monetários para evitar erros de arredondamento.
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    private String paymentMethod; // Ex: "CREDIT_CARD"

    // ID da transação no gateway de pagamento (Stripe, Mercado Pago, etc.)
    // Essencial para rastreamento e reembolsos.
    @Column(name = "gateway_transaction_id", unique = true)
    private String gatewayTransactionId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}