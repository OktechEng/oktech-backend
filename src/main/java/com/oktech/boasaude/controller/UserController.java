package com.oktech.boasaude.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oktech.boasaude.dto.UserResponseDto;
import com.oktech.boasaude.entity.User;
import com.oktech.boasaude.service.UserService;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserResponseDto> getUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            logger.warn("User not authenticated");
            return ResponseEntity.status(401).build();
        }

        User user = (User) authentication.getPrincipal();

        logger.info("User retrieved successfully with ID: {}", user.getId());
        return ResponseEntity.ok(new UserResponseDto(user));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            logger.warn("Unauthorized delete attempt");
            return ResponseEntity.status(401).build();
        }

        User loggedUser = (User) authentication.getPrincipal();

        // Se quiser que o usuário só possa deletar a própria conta
        if (!loggedUser.getId().equals(id)) {
            logger.warn("User {} attempted to delete another account {}", loggedUser.getId(), id);
            return ResponseEntity.status(403).build();
        }

        userService.deleteUser(id);
        logger.info("User with ID {} deleted (soft delete)", id);

        return ResponseEntity.noContent().build();
    }

}
