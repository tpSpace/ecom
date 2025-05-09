package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ProductRequest;
import com.example.ecommerce.dto.ProductResponse;
import com.example.ecommerce.dto.ProductImageResponse;
import com.example.ecommerce.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "ProductController", description = "Product Management")
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create new product", description = "Creates a new product in the database")
    @ApiResponse(responseCode = "201", description = "Product created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid product data")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(
            @ModelAttribute ProductRequest productRequest,
            @RequestParam(value = "images", required = false) MultipartFile[] images) {
        log.info("Request to create new product: {}", productRequest.getName());
        ProductResponse createdProduct = productService.createProductWithImagesAndMap(productRequest, images);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve products with filtering and pagination")
    @ApiResponse(responseCode = "200", description = "List of products")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        
        try {
            log.info("Getting products with filters - page: {}, size: {}, keyword: {}, category: {}, price range: {}-{}", 
                page, size, keyword, categoryId, minPrice, maxPrice);
            
            Page<ProductResponse> products = productService.searchProducts(
                page, size, keyword, categoryId, minPrice, maxPrice, sortBy, direction);
            
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            log.error("Error while fetching products: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while fetching products", e);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a product by its ID")
    @ApiResponse(responseCode = "200", description = "Product found")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
        log.info("Getting product by ID: {}", id);
        ProductResponse product = productService.getProductByIdAsDto(id);
        log.info("Retrieved product: {}", product.getName());

        return ResponseEntity.ok(product);
    }

    @GetMapping("/{id}/details")
    @Operation(summary = "Get product details", description = "Retrieve a product's details without image binary data")
    @ApiResponse(responseCode = "200", description = "Product found")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<ProductResponse> getProductDetails(@PathVariable UUID id) {
        log.info("Getting product details (without images) by ID: {}", id);
        ProductResponse product = productService.getProductDetailsWithoutImages(id);
        log.info("Retrieved product details: {}", product.getName());

        return ResponseEntity.ok(product);
    }

    @GetMapping("/{id}/images")
    @Operation(summary = "Get product images", description = "Retrieve a list of all images for a specific product")
    @ApiResponse(responseCode = "200", description = "List of product images")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<List<ProductImageResponse>> getProductImages(@PathVariable UUID id) {
        log.info("Getting images for product ID: {}", id);
        List<ProductImageResponse> images = productService.getProductImagesMetadata(id);
        log.info("Retrieved {} images for product ID: {}", images.size(), id);

        return ResponseEntity.ok(images);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update product", description = "Updates an existing product")
    @ApiResponse(responseCode = "200", description = "Product updated successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable UUID id,
            @ModelAttribute ProductRequest productRequest,
            @RequestParam(value = "images", required = false) MultipartFile[] images) {

        log.info("Updating product with ID: {}", id);
        ProductResponse updatedProduct = productService.updateProductWithImagesAndMap(id, productRequest, images);
        log.info("Product updated successfully: {}", updatedProduct.getName());

        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping
    @Operation(summary = "Delete product", description = "Deletes a product by its ID")
    @ApiResponse(responseCode = "204", description = "Product deleted successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@RequestParam UUID id) {
        log.info("Deleting product with ID: {}", id);
        productService.deleteProductWithValidation(id);
        log.info("Product deleted successfully: {}", id);

        return ResponseEntity.noContent().build();
    }
}