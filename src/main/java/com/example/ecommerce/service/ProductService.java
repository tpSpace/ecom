package com.example.ecommerce.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.model.ProductModel;
import com.example.ecommerce.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductModel> getProductsByCategory(UUID category) {
        return productRepository.findByCategory_Id(category);
    }

    public ProductModel createProduct(ProductModel product) {
        return productRepository.save(product);
    }
    public ProductModel updateProduct(ProductModel product) {
        return productRepository.save(product);
    }
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }
        
    public List<ProductModel> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<ProductModel> getProductById(UUID id) {
        return productRepository.findById(id);
    }

    public List<ProductModel> getProductsByCategoryId(UUID categoryId) {
        return productRepository.findByCategory_Id(categoryId);
    }

    public List<ProductModel> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }
}