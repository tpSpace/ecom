package com.example.ecommerce.service;

import com.example.ecommerce.dto.CategoryRequest;
import com.example.ecommerce.dto.CategoryResponse;
import com.example.ecommerce.mapper.CategoryMapper;
import com.example.ecommerce.model.CategoryModel;
import com.example.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper mapper;

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public CategoryResponse getCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        CategoryModel entity = mapper.toEntity(request);
        if (request.getParentId() != null) {
            CategoryModel parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent not found: " + request.getParentId()));
            entity.setParent(parent);
        }
        CategoryModel saved = categoryRepository.save(entity);
        return mapper.toDto(saved);
    }

    @Transactional
    public CategoryResponse updateCategory(UUID id, CategoryRequest request) {
        CategoryModel existing = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));
        existing.setName(request.getName().trim());
        existing.setDescription(request.getDescription());
        if (request.getParentId() != null) {
            CategoryModel parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent not found: " + request.getParentId()));
            existing.setParent(parent);
        } else {
            existing.setParent(null);
        }
        CategoryModel updated = categoryRepository.save(existing);
        return mapper.toDto(updated);
    }

    public void deleteCategory(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Category not found: " + id);
        }
        categoryRepository.deleteById(id);
    }
}