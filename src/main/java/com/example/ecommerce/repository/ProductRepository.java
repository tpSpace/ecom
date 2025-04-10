package com.example.ecommerce.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.model.ProductModel;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {
    // Custom query methods can be defined here if needed
    // For example, to find products by category:
    
    @Query("SELECT p FROM ProductModel p WHERE p.productCategory = ?1")
    public List<ProductModel> findByProductCategory(String category);
 
    @Query("SELECT p FROM ProductModel p WHERE p.productName LIKE %?1%")
    public List<ProductModel> findByProductNameContaining(String name);

    @Query("SELECT p FROM ProductModel p WHERE p.productDescription LIKE %?1%")
    public List<ProductModel> findByProductDescriptionContaining(String description);

    @Query("SELECT p FROM ProductModel p WHERE p.productPrice BETWEEN ?1 AND ?2")
    public List<ProductModel> findByProductPriceBetween(double minPrice, double maxPrice);

    @Query("SELECT p FROM ProductModel p WHERE p.averageRating BETWEEN ?1 AND ?2")
    public List<ProductModel> findByAverageRatingBetween(double minRating, double maxRating);

    @Query("SELECT p FROM ProductModel p WHERE p.productCategory = ?1 AND p.productName LIKE %?2%")
    public List<ProductModel> findByProductCategoryAndProductNameContaining(String category, String name);

    @Query("SELECT p FROM ProductModel p WHERE p.productCategory = ?1 AND p.productDescription LIKE %?2%")
    public List<ProductModel> findByProductCategoryAndProductDescriptionContaining(String category, String description);

    @Query("SELECT p FROM ProductModel p WHERE p.productCategory = ?1 AND p.productPrice BETWEEN ?2 AND ?3")
    public List<ProductModel> findByProductCategoryAndProductPriceBetween(String category, double minPrice, double maxPrice);

    // deleteByUUID method
    @Query("DELETE FROM ProductModel p WHERE p.id = ?1")
    public void deleteByUUID(UUID uuid);
}
