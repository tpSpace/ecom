package com.example.ecommerce.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce.model.CategoryModel;

public interface CategoryRepository extends JpaRepository<CategoryModel, UUID> {
    // Add any custom query methods if needed
    // For example, find category by name
    CategoryModel findByName(String name);

}
