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
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.model.CategoryModel;
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

    @Autowired
    private ProductMapper mapper;

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
    public ProductModel createProductWithImages(ProductRequest req, MultipartFile[] images) {
        // 1) Map + validate core fields
        ProductModel product = mapper.toEntity(req);

        // 2) Lookup & set category
        UUID catId;
        try {
            catId = UUID.fromString(req.getCategory());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category ID format: " + req.getCategory());
        }
        CategoryModel category = categoryRepository.findById(catId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + catId));
        product.setCategory(category);

        // 3) Persist product
        product = productRepository.save(product);

        // 4) Handle images (unchanged)
        if (images != null) {
            for (MultipartFile image : images) {
                if (image.isEmpty())
                    continue;
                try {
                    ProductImageModel img = new ProductImageModel();
                    img.setProduct(product);
                    img.setImageData(image.getBytes());
                    productImageRepository.save(img);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save image: " + e.getMessage(), e);
                }
            }
        }
        return product;
    }

    @Transactional
    public ProductModel updateProductWithImages(
            UUID id,
            ProductRequest req,
            MultipartFile[] images) {
        // 1) fetch existing product
        ProductModel product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        // 2) update core fields
        product.setName(req.getName().trim());
        product.setDescription(req.getDescription().trim());
        product.setPrice(req.getPrice());
        product.setQuantity(req.getQuantity());

        // 3) lookup & set category
        UUID catId;
        try {
            catId = UUID.fromString(req.getCategory());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category ID format: " + req.getCategory());
        }
        CategoryModel category = categoryRepository.findById(catId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + catId));
        product.setCategory(category);

        // 4) persist product changes
        product = productRepository.save(product);

        // 5) replace images if new ones provided
        if (images != null) {
            // remove all old images
            productImageRepository.deleteAll(product.getProductImages());
            product.getProductImages().clear();

            // save new uploads
            for (MultipartFile image : images) {
                if (image.isEmpty())
                    continue;
                try {
                    ProductImageModel img = new ProductImageModel();
                    img.setProduct(product);
                    img.setImageData(image.getBytes());
                    productImageRepository.save(img);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save image: " + e.getMessage(), e);
                }
            }
        }

        return product;
    }
}