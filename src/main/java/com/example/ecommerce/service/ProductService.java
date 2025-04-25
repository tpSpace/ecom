package com.example.ecommerce.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.dto.ProductRequest;
import com.example.ecommerce.model.ProductImageModel;
import com.example.ecommerce.model.ProductModel;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductImageRepository;
import com.example.ecommerce.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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

    public Page<ProductModel> getAllProducts(int page, int size) {
        PageRequest pg = PageRequest.of(page, size);
        return productRepository.findAll(pg);
    }

    @Transactional
    public ProductModel createProductWithImages(ProductRequest productRequest, List<MultipartFile> images) {
        // Step 1: Map and save product first
        ProductModel product = mapProductFromRequest(productRequest);
        product = productRepository.save(product);

        // Step 2: Process and save images if provided
        if (images != null && !images.isEmpty()) {
            for (MultipartFile imageFile : images) {
                try {
                    ProductImageModel productImage = new ProductImageModel();
                    productImage.setProduct(product); // Set the saved product
                    productImage.setImageData(imageFile.getBytes());

                    // Generate URL based on product ID and image name
                    String fileName = UUID.randomUUID().toString();
                    productImage.setImageUrl("/images/products/" + product.getId() + "/" + fileName);

                    productImageRepository.save(productImage);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to process image", e);
                }
            }
        }

        return product;
    }

    // Helper method for mapping
    private ProductModel mapProductFromRequest(ProductRequest request) {
        ProductModel product = new ProductModel();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());

        // Map category
        try {
            UUID categoryId = UUID.fromString(request.getCategory());
            categoryRepository.findById(categoryId)
                    .ifPresent(product::setCategory);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid category ID format");
        }

        return product;
    }
}