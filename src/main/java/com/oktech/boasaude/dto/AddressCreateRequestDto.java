package com.oktech.boasaude.dto;

/**
 * DTO para receber os dados na criação de um novo endereço.
 */
public record AddressCreateRequestDto(
    String street,
    String city,
    String state,
    String complement,
    String cep
) {}