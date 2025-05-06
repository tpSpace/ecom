package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.ProductRequest;
import com.example.ecommerce.model.CategoryModel;
import com.example.ecommerce.model.ProductModel;
import com.example.ecommerce.repository.CategoryRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = {})
public abstract class ProductMapperMS {

    @Autowired
    protected CategoryRepository categoryRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", expression = "java(mapCategory(productRequest.getCategory()))")
    @Mapping(target = "images", ignore = true)
    public abstract ProductModel toEntity(ProductRequest productRequest);

    // Helper method to map category string to CategoryModel
    protected CategoryModel mapCategory(String categoryId) {
        try {
            UUID id = UUID.fromString(categoryId);
            return categoryRepository.findById(id).orElse(null);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}