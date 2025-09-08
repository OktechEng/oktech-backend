package com.oktech.boasaude.dto;

import java.util.UUID;

import com.oktech.boasaude.entity.Address;

/**
 * DTO para a resposta da API contendo os dados de um endereço.
 */
public record AddressResponseDto(
    UUID id,
    String zipCode,
    String street,
    String number,
    String complement,
    String neighborhood,
    String city,
    String state,
    String addressType
) {
    // Construtor para facilitar o mapeamento da Entidade para o DTO
    public AddressResponseDto(Address address) {
        this(
            address.getId(),
            address.getZipCode(),
            address.getStreet(),
            address.getNumber(),
            address.getComplement(),
            address.getNeighborhood(),
            address.getCity(),
            address.getState(),
            address.getAddressType()
        );
    }

}