package com.oktech.boasaude.dto;

import com.oktech.boasaude.entity.Address;
import java.util.UUID;

/**
 * DTO para a resposta da API contendo os dados de um endereço.
 */
public record AddressResponseDto(
    UUID id,
    String street,
    String city,
    String state,
    String complement,
    String cep
) {
    /**
     * Construtor que converte uma entidade Address em um AddressResponseDto.
     *
     * @param address A entidade Address a ser convertida.
     */
    public AddressResponseDto(Address address) {
        this(
            address.getId(),
            address.getStreet(),
            address.getCity(),
            address.getState(),
            address.getComplement(),
            address.getCep()
        );
    }
}