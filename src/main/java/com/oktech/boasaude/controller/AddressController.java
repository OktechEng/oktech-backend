package com.oktech.boasaude.controller;

import com.oktech.boasaude.dto.AddressCreateRequestDto;
import com.oktech.boasaude.dto.AddressResponseDto;
import com.oktech.boasaude.entity.User;
import com.oktech.boasaude.service.AddressService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/addresses") 
public class AddressController {

    private final AddressService addressService;
    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<AddressResponseDto> createAddress(
            @Valid @RequestBody AddressCreateRequestDto createDto,
            Authentication authentication) {
        try {
            if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
                logger.warn("Tentativa de criação de endereço por usuário não autenticado.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            User currentUser = (User) authentication.getPrincipal();
            
            AddressResponseDto newAddress = addressService.createAddress(createDto, currentUser);
            
            logger.info("Endereço criado com sucesso com ID: {}", newAddress.id());
            return ResponseEntity.status(HttpStatus.CREATED).body(newAddress);

        } catch (Exception e) {
            logger.error("Erro ao criar endereço: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<List<AddressResponseDto>> getMyAddresses(Authentication authentication) {
        try {
            if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
                logger.warn("Tentativa de buscar endereços por usuário não autenticado.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            User currentUser = (User) authentication.getPrincipal();

            List<AddressResponseDto> addresses = addressService.getAddressesByUser(currentUser);
            
            logger.info("Endereços do usuário {} recuperados com sucesso. Quantidade: {}", currentUser.getId(), addresses.size());
            return ResponseEntity.ok(addresses);
            
        } catch (Exception e) {
            logger.error("Erro ao buscar endereços do usuário: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressResponseDto> getAddressById(
            @PathVariable UUID addressId,
            Authentication authentication) {
        try {
            if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
                logger.warn("Tentativa de buscar endereço por ID por usuário não autenticado.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            User currentUser = (User) authentication.getPrincipal();

            AddressResponseDto address = addressService.getAddressById(addressId, currentUser);
            
            logger.info("Endereço com ID {} recuperado com sucesso.", addressId);
            return ResponseEntity.ok(address);

        } catch (Exception e) {
            logger.error("Erro ao buscar endereço com ID {}: {}", addressId, e.getMessage(), e);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Map<String, String>> deleteAddress(
            @PathVariable UUID addressId,
            Authentication authentication) {
        try {
            if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
                logger.warn("Tentativa de deletar endereço por usuário não autenticado.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            User currentUser = (User) authentication.getPrincipal();
            
            addressService.deleteAddress(addressId, currentUser);
            
            logger.info("Endereço com ID {} deletado com sucesso.", addressId);
            return ResponseEntity.ok(Map.of("message", "Endereço deletado com sucesso"));

        } catch (Exception e) {
            logger.error("Erro ao deletar endereço com ID {}: {}", addressId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao deletar endereço"));
        }
    }
}