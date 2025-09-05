package com.oktech.boasaude.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oktech.boasaude.dto.CreateProductDto;
import com.oktech.boasaude.dto.ProductResponseDto;
import com.oktech.boasaude.entity.Product;
import com.oktech.boasaude.entity.User;
/**
 * ProductService é responsável por gerenciar as operações relacionadas aos produtos.
 * Ele pode incluir funcionalidades como listar produtos, buscar detalhes de um produto específico,
 * criar novos produtos, atualizar informações de produtos existentes, etc.
 * @author João Martins
 * @version 1.0 
 * @author Lucas do Ouro
 * @version 1.1 - Adicionado método para obter produtos por ID de loja.
 * @author Helder Silva
 * @version 1.2 - Adicionado método para buscar produtos com filtros opcionais.
 * 
 */


public interface ProductService {
    
    ProductResponseDto createProduct(CreateProductDto createProductDto, UUID shopId, User currentUser);

    Product getProductById(UUID id);
    ProductResponseDto getProductByIdResponse(UUID id);

    Page<ProductResponseDto> getAllProducts(Pageable pageable);

    ProductResponseDto updateProduct(UUID id, CreateProductDto product, User currentUser);

    void deleteProduct(UUID id, User currentUser);

    Page<ProductResponseDto> getProductsByShopId(UUID shopId, Pageable pageable);


}