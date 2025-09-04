package com.oktech.boasaude.repository;

import java.util.List;
import java.util.UUID;

import com.oktech.boasaude.entity.Address;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * * Repository interface for managing Address entities.
 * This interface extends JpaRepository to provide CRUD operations
 * and custom query methods for Address entities.
 * @author Lucas Ouro
 * @version 1.0
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {
    
    /**
     * Encontra todos os endereços associados a um ID de usuário específico.
     * @param userId O UUID do usuário.
     * @return Uma lista de endereços pertencentes ao usuário.
     */
    List<Address> findByUser_Id(UUID userId);
}