package com.oktech.boasaude.controller;

import com.oktech.boasaude.dto.AdminUserViewDto;
import com.oktech.boasaude.dto.UpdateUserStatusDto;
import com.oktech.boasaude.service.AdminService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // Segurança a nível de classe! Todos os endpoints aqui exigem ADMIN.
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<Page<AdminUserViewDto>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllUsers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminUserViewDto> getUserDetails(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.getUserDetails(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AdminUserViewDto> updateUserStatus(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateUserStatusDto statusDto) {
        return ResponseEntity.ok(adminService.updateUserStatus(id, statusDto.status()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}