package com.example.ecommerce.repository;

import java.util.List;
import java.util.UUID;

import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.model.ProductModel;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, UUID> {

    List<ProductModel> findByCategory_Id(UUID categoryId);
    List<ProductModel> findByCategory_IdAndProductNameContainingIgnoreCase(UUID categoryId, String keyword);
    Double findPriceById(UUID id);
    List<ProductModel> findByCategory(String category);
    Page<ProductModel> findByProductNameContainingIgnoreCase(String keyword, Pageable pageable);

    Page<ProductModel> findByCategory_Id(UUID categoryId, Pageable pageable);
    Page<ProductModel> findByCategory_IdAndProductNameContainingIgnoreCase(UUID categoryId, String keyword, Pageable pageable);

    List<ProductModel> findByNameContainingIgnoreCase(String query);
}