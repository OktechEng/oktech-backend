package com.oktech.boasaude.service;

import java.util.List;
import java.util.UUID;

import com.oktech.boasaude.dto.AddressCreateRequestDto;
import com.oktech.boasaude.dto.AddressResponseDto;
import com.oktech.boasaude.entity.User;

/**
 * Interface for AddressService that defines methods for managing addresses.
 * This service provides methods to create, retrieve, and delete addresses
 * for a user.
 * 
 * @author Lucas Ouro
 * @version 1.0
 */

public interface AddressService {

    AddressResponseDto createAddress(AddressCreateRequestDto createDto, User currentUser);

    AddressResponseDto getAddressById(UUID addressId, User currentUser);
    
    // --- ADICIONE ESTE MÉTODO ---
    AddressResponseDto updateAddress(UUID addressId, AddressCreateRequestDto updateDto, User currentUser);
    // ----------------------------

    List<AddressResponseDto> getAddressesByUser(User currentUser);

    void deleteAddress(UUID addressId, User currentUser);
}