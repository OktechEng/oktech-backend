package com.oktech.boasaude.dto;

/**
 * DTO para receber os dados na criação de um novo endereço.
 */
// Use Records do Java para DTOs - é mais limpo e moderno
public record AddressCreateRequestDto(
    String zipCode,
    String street,
    String number,
    String complement,
    String neighborhood,
    String city,
    String state,
    String addressType
) {}