package com.oktech.boasaude.dto;

import java.util.List;

public record ProductImageUploadResponseDto(
    List<ProductImageResponseDto> uploadedImages,
    int totalImagesForProduct,
    int remainingSlots,
    boolean canAddMore
) {
    public static ProductImageUploadResponseDto create(
            List<ProductImageResponseDto> uploadedImages, 
            long totalImagesForProduct) {
        
        int totalImages = (int) totalImagesForProduct;
        int remainingSlots = Math.max(0, 5 - totalImages);
        boolean canAddMore = remainingSlots > 0;
        
        return new ProductImageUploadResponseDto(
            uploadedImages,
            totalImages,
            remainingSlots,
            canAddMore
        );
    }
}
