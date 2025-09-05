package com.oktech.boasaude.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oktech.boasaude.dto.UserResponseDto;
import com.oktech.boasaude.entity.User;
import com.oktech.boasaude.service.UserService;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<UserResponseDto> getUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            logger.warn("User not authenticated");
            return ResponseEntity.status(401).build();
        }

        User user = (User) authentication.getPrincipal();

        logger.info("User retrieved successfully with ID: {}", user.getId());
        return ResponseEntity.ok(new UserResponseDto(user));
    }
    
    @DeleteMapping("/me") // Endpoint claro para "minha conta"
    public ResponseEntity<Void> deleteOwnAccount(@AuthenticationPrincipal User currentUser) {
        // @AuthenticationPrincipal é a forma do Spring de injetar o usuário logado. Mais limpo e seguro.
        userService.deleteUserById(currentUser.getId());
        return ResponseEntity.noContent().build();
    }

}
