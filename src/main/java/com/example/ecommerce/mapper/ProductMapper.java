package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.ProductRequest;
import com.example.ecommerce.dto.ProductResponse;
import com.example.ecommerce.dto.ProductImageResponse;
import com.example.ecommerce.model.ProductModel;
import com.example.ecommerce.model.ProductImageModel;

import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProductMapper {

    /**
     * Convert DTO to entity
     */
    public ProductModel toEntity(ProductRequest dto) {
        if (dto == null)
            return null;

        ProductModel entity = new ProductModel();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setQuantity(dto.getQuantity());
        entity.setSku(dto.getSku());

        return entity;
    }

    /**
     * Convert entity to response DTO with images
     */
    public ProductResponse toResponseDto(ProductModel entity) {
        if (entity == null)
            return null;

        ProductResponse dto = new ProductResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setQuantity(entity.getQuantity());
        dto.setSku(entity.getSku());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
            dto.setCategoryName(entity.getCategory().getName());
        }

        // Map product images to DTOs
        if (entity.getProductImages() != null && !entity.getProductImages().isEmpty()) {
            dto.setImages(entity.getProductImages().stream()
                    .map(this::toImageDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    /**
     * Convert entity to response DTO without images
     */
    public ProductResponse toResponseDtoWithoutImages(ProductModel entity) {
        if (entity == null)
            return null;

        ProductResponse dto = new ProductResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setQuantity(entity.getQuantity());
        dto.setSku(entity.getSku());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
            dto.setCategoryName(entity.getCategory().getName());
        }

        // Don't include images

        return dto;
    }

    /**
     * Convert image entity to DTO
     */
    private ProductImageResponse toImageDto(ProductImageModel image) {
        if (image == null)
            return null;

        ProductImageResponse dto = new ProductImageResponse();
        dto.setId(image.getId());
        dto.setProductId(image.getProduct().getId());
        dto.setImageData(image.getImageData());

        return dto;
    }
}