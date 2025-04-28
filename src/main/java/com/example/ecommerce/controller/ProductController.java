package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ProductRequest;
import com.example.ecommerce.dto.ProductResponse;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.model.ProductModel;
import com.example.ecommerce.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "ProductController", description = "Product Management")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper mapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create new product", description = "Creates a new product in the database")
    @ApiResponse(responseCode = "201", description = "Product created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid product data")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createProduct(
            @ModelAttribute ProductRequest productRequest,
            @RequestParam(value = "images", required = false) MultipartFile[] images) {

        try {
            // Create product and handle images
            ProductModel createdProduct = productService.createProductWithImages(
                    productRequest, images);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Rest of error handling remains the same
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid input data");
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An unexpected error occurred");
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get all products
    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve all products with pagination")
    @ApiResponse(responseCode = "200", description = "List of products")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ProductModel> models = productService.getAllProducts(page, size);
        Page<ProductResponse> dtos = models.map(mapper::toResponseDto);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/by-id")
    @Operation(summary = "Get product by ID", description = "Retrieve a product by its ID")
    @ApiResponse(responseCode = "200", description = "Product found")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<ProductModel> getProductById(@RequestParam UUID id) {
        return productService.getProductById(id)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/by-category")
    @Operation(summary = "Get products by category", description = "Retrieve products by category ID")
    @ApiResponse(responseCode = "200", description = "List of products in category")
    public ResponseEntity<List<ProductModel>> getProductsByCategory(
            @RequestParam UUID categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId, page, size));
    }

    @PutMapping
    @Operation(summary = "Update product", description = "Updates an existing product")
    @ApiResponse(responseCode = "200", description = "Product updated successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductModel> updateProduct(
            @RequestParam UUID id,
            @RequestBody ProductModel product) {
        return productService.getProductById(id)
                .map(existingProduct -> {
                    product.setId(id);
                    return new ResponseEntity<>(productService.updateProduct(product), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping
    @Operation(summary = "Delete product", description = "Deletes a product by its ID")
    @ApiResponse(responseCode = "204", description = "Product deleted successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@RequestParam UUID id) {
        if (productService.getProductById(id).isPresent()) {
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}