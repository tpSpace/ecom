package com.example.ecommerce.service;

import com.example.ecommerce.model.ProductModel;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    
    // Constructor injection (preferred over @Autowired)
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    /**
     * Creates a new product or updates an existing one if ID is provided
     * @param product The product to create/update
     * @return The saved product with generated ID
     */
    public ProductModel createProduct(ProductModel product) {
        // You can add validation or business logic here
        return productRepository.save(product);
    }
    
    public Optional<ProductModel> getProductById(UUID id) {
        return productRepository.findById(id);
    }
    
    public List<ProductModel> getAllProducts() {
        return productRepository.findAll();
    }
    
    public List<ProductModel> getProductsByCategory(UUID categoryId) {
        return productRepository.findByCategory_Id(categoryId);
    }
        
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }
}