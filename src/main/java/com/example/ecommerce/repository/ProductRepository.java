package com.example.ecommerce.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.model.ProductModel;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, UUID> {

    Double findPriceById(UUID id);

    Page<ProductModel> findByCategory_Id(UUID categoryId, Pageable pageable);

    Page<ProductModel> findByCategory_IdAndNameContainingIgnoreCase(UUID categoryId, String name,
            Pageable pageable);

    boolean existsBySku(String sku);

    Optional<ProductModel> findBySku(String sku);

    List<ProductModel> findByNameAndCategory_Id(String name, UUID id);
}