package com.example.ecommerce.mapper;

import org.springframework.stereotype.Component;
import com.example.ecommerce.dto.CategoryRequest;
import com.example.ecommerce.dto.CategoryResponse;
import com.example.ecommerce.model.CategoryModel;

import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public CategoryModel toEntity(CategoryRequest dto) {
        CategoryModel cat = new CategoryModel();
        cat.setName(dto.getName().trim());
        cat.setDescription(dto.getDescription());
        // parent will be set in service
        return cat;
    }

    public CategoryResponse toDto(CategoryModel entity) {
        if (entity == null)
            return null;
        CategoryResponse dto = new CategoryResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setParentId(entity.getParent() != null ? entity.getParent().getId() : null);
        dto.setSubcategories(
                entity.getSubcategories().stream()
                        .map(this::toDto)
                        .collect(Collectors.toList()));
        return dto;
    }
}