package com.example.ecommerce.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.ecommerce.model.ProductModel;
import com.example.ecommerce.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductModel createProduct(ProductModel product) {
        return productRepository.save(product);
    }
    public ProductModel updateProduct(ProductModel product) {
        return productRepository.save(product);
    }
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }
    public Optional<ProductModel> getProductById(UUID id) {
        return productRepository.findById(id);
    }

    public List<ProductModel> getProductsByCategory(UUID categoryId, int page, int size) {
        PageRequest pg = PageRequest.of(page, size);
        Page<ProductModel> productPage = productRepository.findByCategory_Id(categoryId, pg);
        return productPage.getContent();
    }

    public List<ProductModel> getProductByCategoryIdPageable(UUID categoryId, int page, int size) {
        PageRequest pg = PageRequest.of(page, size);
        Page<ProductModel> productPage = productRepository.findByCategory_Id(categoryId, pg);
        return productPage.getContent();
    }
}