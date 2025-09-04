package com.oktech.boasaude.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.oktech.boasaude.dto.ProductImageResponseDto;
import com.oktech.boasaude.dto.ProductImageUploadResponseDto;
import com.oktech.boasaude.entity.ProductImage;
import com.oktech.boasaude.service.impl.ProductImageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/product-images")
@Tag(name = "Product Images", description = "Operações relacionadas às imagens dos produtos")
public class ProductImageController {

    private final ProductImageService productImageService;

    public ProductImageController(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @PostMapping("/upload")
    @Operation(summary = "Upload de imagem(s) para produto", description = "Faz upload de uma ou múltiplas imagens (máximo 5) e as associa a um produto específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Imagem(s) criada(s) com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos, limite excedido ou arquivos vazios"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProductImageUploadResponseDto> uploadImages(
            @Parameter(description = "Arquivo(s) de imagem (máximo 5)", required = true)
            @RequestParam("files") List<MultipartFile> files,
            @Parameter(description = "ID do produto", required = true)
            @RequestParam("productId") UUID productId) {
        
        return handleImageUpload(files, productId);
    }

    @PostMapping(value = "/upload-form", consumes = "multipart/form-data")
    @Operation(summary = "Upload de imagem(s) via form-data", description = "Faz upload de uma ou múltiplas imagens (máximo 5) via form-data e as associa a um produto específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Imagem(s) criada(s) com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou arquivo não fornecido"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProductImageUploadResponseDto> uploadImagesFormData(
            @Parameter(description = "Arquivo(s) de imagem (máximo 5)", required = true)
            @RequestPart("files") List<MultipartFile> files,
            @Parameter(description = "ID do produto", required = true)
            @RequestPart("productId") String productIdStr) {
        
        // Validar entrada
        if (files.isEmpty() || files.stream().allMatch(MultipartFile::isEmpty)) {
            return ResponseEntity.badRequest().build();
        }

        UUID productId;
        try {
            productId = UUID.fromString(productIdStr);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        return handleImageUpload(files, productId);
    }

    private ResponseEntity<ProductImageUploadResponseDto> handleImageUpload(List<MultipartFile> files, UUID productId) {
        try {
            List<ProductImage> savedImages = productImageService.saveFilesWithProduct(files, productId);
            List<ProductImageResponseDto> responseDtos = savedImages.stream()
                    .map(ProductImageResponseDto::fromEntity)
                    .toList();
            
            long totalImagesAfterUpload = productImageService.countImagesByProductId(productId);
            ProductImageUploadResponseDto response = ProductImageUploadResponseDto.create(responseDtos, totalImagesAfterUpload);
            
            return ResponseEntity.status(201).body(response);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Product not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Buscar imagens por produto", description = "Retorna todas as imagens de um produto específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Imagens encontradas com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<List<ProductImageResponseDto>> getImagesByProductId(
            @Parameter(description = "ID do produto", required = true)
            @PathVariable UUID productId) {
        
        List<ProductImage> images = productImageService.getImagesByProductId(productId);
        List<ProductImageResponseDto> imageDtos = images.stream()
                .map(ProductImageResponseDto::fromEntity)
                .toList();
        return ResponseEntity.ok(imageDtos);
    }

    @GetMapping("/{imageId}")
    @Operation(summary = "Buscar imagem por ID", description = "Retorna uma imagem específica pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Imagem encontrada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Imagem não encontrada")
    })
    public ResponseEntity<ProductImageResponseDto> getImageById(
            @Parameter(description = "ID da imagem", required = true)
            @PathVariable UUID imageId) {
        
        return productImageService.getImageById(imageId)
                .map(ProductImageResponseDto::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
