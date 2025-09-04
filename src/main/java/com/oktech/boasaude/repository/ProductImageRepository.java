package com.oktech.boasaude.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oktech.boasaude.entity.Product;
import com.oktech.boasaude.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {
    
    List<ProductImage> findByProduct(Product product);
    
    List<ProductImage> findByProductId(UUID productId);
}
