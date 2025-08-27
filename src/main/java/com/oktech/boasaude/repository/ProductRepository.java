package com.oktech.boasaude.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.oktech.boasaude.entity.Product;

/**
 * Repositório para operações CRUD com a entidade Product.
 * Fornece métodos para buscar produtos por loja, nome e categoria.
 * 
 * @author João Martins
 * @version 1.0
 * @author Lucas do Ouro
 * @version 1.1 - Adicionado método para buscar produtos por ID de loja.
 * @author Helder Araujo
 * @version 1.2 - Adicionado suporte a especificações JPA.
 */

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    Page<Product> findAllByShopId(UUID shopId, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Product> findByCategory(String category, Pageable pageable);

    Page<Product> findByShopId(UUID shopId, Pageable pageable);

}
