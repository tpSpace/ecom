package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductRequest;
import com.example.ecommerce.dto.ProductResponse;
import com.example.ecommerce.dto.ProductImageDto;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.model.CategoryModel;
import com.example.ecommerce.model.ProductModel;
import com.example.ecommerce.model.ProductImageModel;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.ProductImageRepository;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.exception.DuplicateResourceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductMapper mapper;

    /**
     * Get all products with pagination
     */
    public Page<ProductModel> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
    }

    /**
     * Get all products as DTOs with pagination
     */
    public Page<ProductResponse> getAllProductsAsDto(int page, int size) {
        Page<ProductModel> products = getAllProducts(page, size);
        return products.map(mapper::toResponseDto);
    }

    /**
     * Get product by ID
     */
    public Optional<ProductModel> getProductById(UUID id) {
        return productRepository.findById(id);
    }

    /**
     * Get product by ID as DTO
     */
    public ProductResponse getProductByIdAsDto(UUID id) {
        ProductModel product = getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return mapper.toResponseDto(product);
    }

    /**
     * Get product details without images
     */
    public ProductResponse getProductDetailsWithoutImages(UUID id) {
        ProductModel product = getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return mapper.toResponseDtoWithoutImages(product);
    }

    /**
     * Get product images metadata
     */
    public List<ProductImageDto> getProductImagesMetadata(UUID productId) {
        // Validate product exists
        if (!getProductById(productId).isPresent()) {
            throw new ResourceNotFoundException("Product", "id", productId);
        }

        return productImageRepository.findByProductId(productId)
                .stream()
                .map(image -> {
                    ProductImageDto dto = new ProductImageDto();
                    dto.setId(image.getId());
                    dto.setProductId(productId);
                    dto.setImageData(image.getImageData());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Get products by category
     */
    public List<ProductModel> getProductsByCategory(UUID categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByCategory_Id(categoryId, pageable).getContent();
    }

    /**
     * Get products by category as DTOs
     */
    public List<ProductResponse> getProductsByCategoryAsDto(UUID categoryId, int page, int size) {
        // Validate category exists
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category", "id", categoryId);
        }

        List<ProductModel> products = getProductsByCategory(categoryId, page, size);
        return products.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Create product with images
     */
    @Transactional
    public ProductModel createProductWithImages(ProductRequest req, MultipartFile[] images) {
        // Validate request fields
        if (req.getName() == null || req.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }

        if (req.getPrice() == null || req.getPrice() <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero");
        }

        // Check for duplicate SKU
        if (req.getSku() != null && !req.getSku().trim().isEmpty() &&
                productRepository.existsBySku(req.getSku().trim())) {
            throw new DuplicateResourceException("Product", "SKU", req.getSku().trim());
        }

        // Map basic fields
        ProductModel product = mapper.toEntity(req);

        // Lookup & set category
        UUID catId;
        try {
            catId = UUID.fromString(req.getCategory());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category ID format: " + req.getCategory());
        }

        CategoryModel category = categoryRepository.findById(catId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", catId));
        product.setCategory(category);

        // Save product
        product = productRepository.save(product);
        log.debug("Saved product base information: {}", product.getId());

        // Process images
        if (images != null) {
            for (MultipartFile image : images) {
                if (image.isEmpty())
                    continue;
                System.out.println("Image: " + image.getOriginalFilename());
                ProductImageModel img = new ProductImageModel();
                img.setProduct(product);
                try {
                    img.setImageData(image.getBytes());
                } catch (IOException e) {

                }
                productImageRepository.save(img);
                log.debug("Saved image for product {}: {}", product.getId(), image.getOriginalFilename());
            }
        }

        return product;
    }

    /**
     * Create product with images and map to DTO
     */
    @Transactional
    public ProductResponse createProductWithImagesAndMap(ProductRequest req, MultipartFile[] images) {
        ProductModel product = createProductWithImages(req, images);
        return mapper.toResponseDto(product);
    }

    /**
     * Update product with images
     */
    @Transactional
    public ProductModel updateProductWithImages(UUID id, ProductRequest req, MultipartFile[] images) {
        // Validate product exists
        ProductModel product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        // Validate request fields
        if (req.getName() == null || req.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }

        if (req.getPrice() == null || req.getPrice() <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero");
        }

        // Check for duplicate SKU, but allow the same product to keep its SKU
        if (req.getSku() != null && !req.getSku().trim().isEmpty()) {
            Optional<ProductModel> existingProduct = productRepository.findBySku(req.getSku().trim());
            if (existingProduct.isPresent() && !existingProduct.get().getId().equals(id)) {
                throw new DuplicateResourceException("Product", "SKU", req.getSku().trim());
            }
        }

        // Update core fields
        product.setName(req.getName().trim());
        product.setDescription(req.getDescription() != null ? req.getDescription().trim() : "");
        product.setPrice(req.getPrice());
        product.setQuantity(req.getQuantity());
        product.setSku(req.getSku() != null ? req.getSku().trim() : null);

        // Lookup & set category
        UUID catId;
        try {
            catId = UUID.fromString(req.getCategory());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category ID format: " + req.getCategory());
        }

        CategoryModel category = categoryRepository.findById(catId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", catId));
        product.setCategory(category);

        // Update product
        product = productRepository.save(product);
        log.debug("Updated product base information: {}", product.getId());

        // Handle images
        if (images != null && images.length > 0) {
            // Remove old images
            productImageRepository.deleteByProductId(id);
            log.debug("Deleted existing images for product: {}", product.getId());

            // Save new images
            for (MultipartFile image : images) {
                if (image.isEmpty())
                    continue;

                ProductImageModel img = new ProductImageModel();
                img.setProduct(product);
                try {
                    img.setImageData(image.getBytes());
                } catch (IOException e) {
                    log.error("Error while saving image data for product {}: {}", product.getId(), e.getMessage());
                }

                productImageRepository.save(img);
                log.info("Saved new image for product {}: {}", product.getId(), image.getOriginalFilename());
            }
        }

        return product;
    }

    /**
     * Update product with images and map to DTO
     */
    @Transactional
    public ProductResponse updateProductWithImagesAndMap(UUID id, ProductRequest req, MultipartFile[] images) {
        ProductModel product = updateProductWithImages(id, req, images);
        return mapper.toResponseDto(product);
    }

    /**
     * Delete product
     */
    @Transactional
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }

    /**
     * Delete product with validation
     */
    @Transactional
    public void deleteProductWithValidation(UUID id) {
        ProductModel product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        productRepository.delete(product);
        log.info("Deleted product: {}", id);
    }
}