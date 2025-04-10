package com.example.ecommerce.service;

import com.example.ecommerce.model.ProductModel;

import com.example.ecommerce.repository.ProductRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Data
@Tag(name = "ProductService", description = "Product Management")
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    public Optional<ProductModel> getProductById(Long id) {
        return Optional.ofNullable(productRepository.findById(id).orElse(null));
    }
    public List<ProductModel> getAllProducts() {
        return productRepository.findAll();
    }
    public List<ProductModel> getProductsByCategory(String category) {
        return productRepository.findByProductCategory(category);
    }
    public List<ProductModel> getProductsByName(String name) {
        return productRepository.findByProductNameContaining(name);
    }
    public List<ProductModel> getProductsByDescription(String description) {
        return productRepository.findByProductDescriptionContaining(description);
    }
    public List<ProductModel> getProductsByPriceRange(double minPrice, double maxPrice) {
        return productRepository.findByProductPriceBetween(minPrice, maxPrice);
    }
    public List<ProductModel> getProductsByRatingRange(double minRating, double maxRating) {
        return productRepository.findByAverageRatingBetween(minRating, maxRating);
    }
    public List<ProductModel> getProductsByCategoryAndName(String category, String name) {
        return productRepository.findByProductCategoryAndProductNameContaining(category, name);
    }
    public List<ProductModel> getProductsByCategoryAndDescription(String category, String description) {
        return productRepository.findByProductCategoryAndProductDescriptionContaining(category, description);
    }
    public List<ProductModel> getProductsByCategoryAndPriceRange(String category, double minPrice, double maxPrice) {
        return productRepository.findByProductCategoryAndProductPriceBetween(category, minPrice, maxPrice);
    }
    
    public ProductModel createProduct(ProductModel product) {
        return productRepository.save(product);
    }

    public void deleteProduct(UUID uuid) {
        productRepository.deleteByUUID(uuid);
    }
}