package com.example.ecommerce.service;

import com.example.ecommerce.model.CategoryModel;
import com.example.ecommerce.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryModel> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<CategoryModel> getCategoryById(UUID id) {
        return categoryRepository.findById(id);
    }

    public CategoryModel createCategory(CategoryModel category) {
        return categoryRepository.save(category);
    }

    public CategoryModel updateCategory(CategoryModel category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(UUID id) {
        categoryRepository.deleteById(id);
    }
}