package com.oktech.boasaude.repository;

import com.oktech.boasaude.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

import java.util.UUID;

/**
 * Repositório para operações CRUD com a entidade Shop.
 * Fornece métodos para buscar lojas por CNPJ, proprietário e nome.
 * 
 * @author Lucas Ouro
 * @version 1.0
 */

public interface ShopRepository extends JpaRepository<Shop, UUID> {

    Optional<Shop> findByCnpj(String cnpj); // Busca loja por CNPJ

    Optional<Shop> findByOwnerId(UUID ownerId); // Busca loja por ID do proprietário

    boolean existsByCnpj(String cnpj); // Verifica se já existe uma loja com o mesmo CNPJ

    List<Shop> findAllByNameContainingIgnoreCase(String name); // Busca lojas por nome, ignorando maiúsculas e
                                                               // minúsculas

    // Métodos para busca com filtros
    Page<Shop> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    Page<Shop> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);
    
    Page<Shop> findByCnpjContaining(String cnpj, Pageable pageable);
    
    Page<Shop> findByNameContainingIgnoreCaseAndDescriptionContainingIgnoreCase(String name, String description, Pageable pageable);
    
    Page<Shop> findByNameContainingIgnoreCaseAndCnpjContaining(String name, String cnpj, Pageable pageable);
    
    Page<Shop> findByDescriptionContainingIgnoreCaseAndCnpjContaining(String description, String cnpj, Pageable pageable);
    
    Page<Shop> findByNameContainingIgnoreCaseAndDescriptionContainingIgnoreCaseAndCnpjContaining(String name, String description, String cnpj, Pageable pageable);

}
