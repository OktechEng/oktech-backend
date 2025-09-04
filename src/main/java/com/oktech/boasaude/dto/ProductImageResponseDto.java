package com.oktech.boasaude.dto;

import java.util.UUID;

import com.oktech.boasaude.entity.ProductImage;

public record ProductImageResponseDto(
    UUID id,
    String imageUrl,
    UUID productId
) {
    public static ProductImageResponseDto fromEntity(ProductImage productImage) {
        return new ProductImageResponseDto(
            productImage.getId(),
            productImage.getImageUrl(),
            productImage.getProduct() != null ? productImage.getProduct().getId() : null
        );
    }
}
