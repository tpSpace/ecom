package com.example.ecommerce.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.model.ProductModel;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, UUID> {

    List<ProductModel> findByCategory_Id(UUID categoryId);
        
    List<ProductModel> findByProductNameContainingIgnoreCase(String keyword);
}