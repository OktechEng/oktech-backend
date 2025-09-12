package com.oktech.boasaude.controller;

import com.oktech.boasaude.dto.CreateSubscriptionDto;
import com.oktech.boasaude.dto.SubscriptionViewDto;
import com.oktech.boasaude.dto.UpdateSubscriptionStatusDto;
import com.oktech.boasaude.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/admin/subscriptions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin - Subscriptions", description = "Endpoints para gerenciamento de assinaturas")
public class AdminSubscriptionController {

    private final AdminService adminService;

    @GetMapping
    @Operation(summary = "Listar todas as assinaturas", description = "Retorna uma lista paginada de todas as assinaturas")
    public ResponseEntity<Page<SubscriptionViewDto>> getAllSubscriptions(Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllSubscriptions(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar assinatura por ID", description = "Retorna os detalhes de uma assinatura específica")
    public ResponseEntity<SubscriptionViewDto> getSubscriptionById(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.getSubscriptionDetails(id));
    }

    @PostMapping
    @Operation(summary = "Criar nova assinatura", description = "Cria uma nova assinatura com os dados fornecidos")
    public ResponseEntity<SubscriptionViewDto> createSubscription(@RequestBody @Valid CreateSubscriptionDto createDto) {
        return ResponseEntity.ok(adminService.createSubscription(createDto));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status da assinatura", description = "Atualiza o status (ativo/inativo) de uma assinatura")
    public ResponseEntity<SubscriptionViewDto> updateSubscriptionStatus(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateSubscriptionStatusDto statusDto) {
        return ResponseEntity.ok(adminService.updateSubscriptionStatus(id, statusDto.active()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir assinatura", description = "Remove uma assinatura do sistema")
    public ResponseEntity<Void> deleteSubscription(@PathVariable UUID id) {
        adminService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar assinaturas por nome do cliente", description = "Retorna uma lista paginada de assinaturas que correspondem ao nome do cliente")
    public ResponseEntity<Page<SubscriptionViewDto>> searchSubscriptions(
            @RequestParam String customerName,
            Pageable pageable) {
        return ResponseEntity.ok(adminService.findSubscriptionsByCustomerName(customerName, pageable));
    }

    @GetMapping("/expiring")
    @Operation(summary = "Listar assinaturas expirando", description = "Retorna uma lista de assinaturas que vão expirar nos próximos X dias")
    public ResponseEntity<List<SubscriptionViewDto>> getExpiringSubscriptions(
            @RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(adminService.findExpiringSubscriptions(days));
    }
}