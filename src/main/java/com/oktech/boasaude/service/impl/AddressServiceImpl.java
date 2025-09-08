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
        // Mapeamento explícito no serviço - melhor para separação de responsabilidades
        Address newAddress = new Address();
        newAddress.setUser(currentUser); // Associa o endereço ao usuário logado
        newAddress.setZipCode(createDto.zipCode());
        newAddress.setStreet(createDto.street());
        newAddress.setNumber(createDto.number());
        newAddress.setComplement(createDto.complement());
        newAddress.setNeighborhood(createDto.neighborhood());
        newAddress.setCity(createDto.city());
        newAddress.setState(createDto.state());
        newAddress.setAddressType(createDto.addressType());

        Address savedAddress = addressRepository.save(newAddress);
        return new AddressResponseDto(savedAddress);
    }

    @Override
    public AddressResponseDto getAddressById(UUID addressId, User currentUser) {
        Address address = findAddressAndCheckOwnership(addressId, currentUser);
        return new AddressResponseDto(address);
    }

    // --- NOVO MÉTODO DE ATUALIZAÇÃO ---
    @Override
    public AddressResponseDto updateAddress(UUID addressId, AddressCreateRequestDto updateDto, User currentUser) {
        // 1. Reutiliza nosso método seguro para buscar e verificar a posse do endereço
        Address existingAddress = findAddressAndCheckOwnership(addressId, currentUser);

        // 2. Atualiza os campos da entidade com os dados do DTO
        existingAddress.setZipCode(updateDto.zipCode());
        existingAddress.setStreet(updateDto.street());
        existingAddress.setNumber(updateDto.number());
        existingAddress.setComplement(updateDto.complement());
        existingAddress.setNeighborhood(updateDto.neighborhood());
        existingAddress.setCity(updateDto.city());
        existingAddress.setState(updateDto.state());
        existingAddress.setAddressType(updateDto.addressType());

        // 3. Salva a entidade atualizada e retorna o DTO de resposta
        Address updatedAddress = addressRepository.save(existingAddress);
        return new AddressResponseDto(updatedAddress);
    }

    @Override
    public List<AddressResponseDto> getAddressesByUser(User currentUser) {
        return addressRepository.findByUser_Id(currentUser.getId()).stream()
                .map(AddressResponseDto::new)
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
     * (Este método está perfeito, não precisa de alterações)
     */
    private Address findAddressAndCheckOwnership(UUID addressId, User currentUser) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Endereço com ID " + addressId + " não encontrado."));

        if (!address.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Acesso negado. Este endereço não pertence ao usuário autenticado.");
        }
        
        return address;
    }
}