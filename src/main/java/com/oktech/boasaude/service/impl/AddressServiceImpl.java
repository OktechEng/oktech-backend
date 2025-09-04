package com.oktech.boasaude.service.impl;

import com.oktech.boasaude.dto.AddressCreateRequestDto;
import com.oktech.boasaude.dto.AddressResponseDto;
import com.oktech.boasaude.entity.Address;
import com.oktech.boasaude.entity.User;

import com.oktech.boasaude.service.AddressService;

import com.oktech.boasaude.repository.AddressRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public AddressResponseDto createAddress(AddressCreateRequestDto createDto, User currentUser) {
        // Usa o construtor que criamos na entidade Address para mapear o DTO
        Address newAddress = new Address(createDto, currentUser);
        Address savedAddress = addressRepository.save(newAddress);
        // Usa o construtor do DTO de resposta para mapear a entidade
        return new AddressResponseDto(savedAddress);
    }

    @Override
    public AddressResponseDto getAddressById(UUID addressId, User currentUser) {
        Address address = findAddressAndCheckOwnership(addressId, currentUser);
        return new AddressResponseDto(address);
    }

    @Override
    public List<AddressResponseDto> getAddressesByUser(User currentUser) {
        return addressRepository.findByUser_Id(currentUser.getId()).stream()
                .map(AddressResponseDto::new) // Converte cada Address para AddressResponseDto
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAddress(UUID addressId, User currentUser) {
        Address addressToDelete = findAddressAndCheckOwnership(addressId, currentUser);
        addressRepository.delete(addressToDelete);
    }

    /**
     * Método auxiliar para buscar um endereço e verificar se ele pertence ao usuário logado.
     * Centraliza a lógica de busca e a verificação de segurança.
     */
    private Address findAddressAndCheckOwnership(UUID addressId, User currentUser) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Endereço com ID " + addressId + " não encontrado."));

        // Verificação de segurança crucial!
        if (!address.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Acesso negado. Este endereço não pertence ao usuário autenticado.");
        }
        
        return address;
    }
}