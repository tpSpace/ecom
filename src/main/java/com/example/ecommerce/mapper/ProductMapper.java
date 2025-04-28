package com.example.ecommerce.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.ecommerce.dto.ProductRequest;
import com.example.ecommerce.dto.ProductResponse;
import com.example.ecommerce.model.ProductModel;

@Component
public class ProductMapper {

    public ProductModel toEntity(ProductRequest dto) {
        if (dto == null) {
            throw new IllegalArgumentException("ProductRequest must not be null");
        }

        // 1) Validate required text fields
        if (!StringUtils.hasText(dto.getName())) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (!StringUtils.hasText(dto.getDescription())) {
            throw new IllegalArgumentException("Product description is required");
        }
        if (!StringUtils.hasText(dto.getCategory())) {
            throw new IllegalArgumentException("Category ID is required");
        }

        // 2) Validate numeric fields
        if (dto.getPrice() == null || dto.getPrice() < 0) {
            throw new IllegalArgumentException("Price must be a non‑negative value");
        }
        if (dto.getQuantity() == null || dto.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity must be a non‑negative integer");
        }

        // 3) Map values
        ProductModel product = new ProductModel();
        product.setName(dto.getName().trim());
        product.setDescription(dto.getDescription().trim());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        // category is set later in service, so leave it null here
        return product;
    }

    public ProductRequest toRequestDto(ProductModel entity) {
        if (entity == null)
            return null;
        ProductRequest dto = new ProductRequest();
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setQuantity(entity.getQuantity());
        if (entity.getCategory() != null) {
            dto.setCategory(entity.getCategory().getId().toString());
        }
        return dto;
    }

    public ProductResponse toResponseDto(ProductModel product) {
        ProductResponse dto = new ProductResponse();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory().getId().toString());
        dto.setQuantity(product.getQuantity());
        dto.setImages(
                product.getProductImages().stream()
                        .map(img -> img.getImageData())
                        .collect(Collectors.toList()));
        return dto;
    }
}