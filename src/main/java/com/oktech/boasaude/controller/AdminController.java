package com.oktech.boasaude.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oktech.boasaude.service.UserService;

@RestController
@RequestMapping("/v1/admin")
public class AdminController {
    private final UserService userService; // Por enquanto, pode usar o UserService

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Mágica do Spring Security! Só Admins entram aqui.
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}