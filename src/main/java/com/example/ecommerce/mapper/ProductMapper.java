package com.example.ecommerce.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.ecommerce.dto.ProductRequest;
import com.example.ecommerce.dto.ProductResponse;
import com.example.ecommerce.model.ProductModel;

@Component
public class ProductMapper {

    public ProductModel toEntity(ProductRequest dto) {
        ProductModel product = new ProductModel();
        product.setName(dto.getName().trim());
        product.setDescription(dto.getDescription().trim());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
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