package com.example.ecommerce.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.model.ProductModel;
import com.example.ecommerce.service.ProductService;


@RestController
@Tag(name = "ProductController", description = "Product Management")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    @Operation(summary = "Get all products", description = "Retrieve a list of all products")
    @ApiResponse(responseCode = "200", description = "List of products")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public List<ProductModel> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{category}")
    @Operation(summary = "Get products by category", description = "Retrieve a list of products by category")
    @ApiResponse(responseCode = "200", description = "List of products by category")
    @ApiResponse(responseCode = "404", description = "Products not found")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public List<ProductModel> getProductsByCategory(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }

    @GetMapping("/products/name/{name}")
    @Operation(summary = "Get products by name", description = "Retrieve a list of products by name")
    @ApiResponse(responseCode = "200", description = "List of products by name")
    @ApiResponse(responseCode = "404", description = "Products not found")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public List<ProductModel> getProductsByName(@PathVariable String name) {
        return productService.getProductsByName(name);
    }

    @GetMapping("/products/description/{description}")
    @Operation(summary = "Get products by description", description = "Retrieve a list of products by description")
    @ApiResponse(responseCode = "200", description = "List of products by description")
    @ApiResponse(responseCode = "404", description = "Products not found")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public List<ProductModel> getProductsByDescription(@PathVariable String description) {
        return productService.getProductsByDescription(description);
    }

    @GetMapping("/products/price/{minPrice}/{maxPrice}")
    @Operation(summary = "Get products by price range", description = "Retrieve a list of products by price range")
    @ApiResponse(responseCode = "200", description = "List of products by price range")
    @ApiResponse(responseCode = "404", description = "Products not found")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public List<ProductModel> getProductsByPriceRange(@PathVariable double minPrice, @PathVariable double maxPrice) {
        return productService.getProductsByPriceRange(minPrice, maxPrice);
    }

    @GetMapping("/products/rating/{minRating}/{maxRating}")
    @Operation(summary = "Get products by rating range", description = "Retrieve a list of products by rating range")
    @ApiResponse(responseCode = "200", description = "List of products by rating range")
    @ApiResponse(responseCode = "404", description = "Products not found")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public List<ProductModel> getProductsByRatingRange(@PathVariable double minRating, @PathVariable double maxRating) {
        return productService.getProductsByRatingRange(minRating, maxRating);
    }


    @GetMapping("/products/category/{category}/name/{name}")
    @Operation(summary = "Get products by category and name", description = "Retrieve a list of products by category and name")
    @ApiResponse(responseCode = "200", description = "List of products by category and name")
    @ApiResponse(responseCode = "404", description = "Products not found")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public List<ProductModel> getProductsByCategoryAndName(@PathVariable String category, @PathVariable String name) {
        return productService.getProductsByCategoryAndName(category, name);
    }

    @GetMapping("/products/category/{category}/description/{description}")
    @Operation(summary = "Get products by category and description", description = "Retrieve a list of products by category and description")
    @ApiResponse(responseCode = "200", description = "List of products by category and description")
    @ApiResponse(responseCode = "404", description = "Products not found")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public List<ProductModel> getProductsByCategoryAndDescription(@PathVariable String category, @PathVariable String description) {
        return productService.getProductsByCategoryAndDescription(category, description);
    }

    @GetMapping("/products/category/{category}/price/{minPrice}/{maxPrice}")
    @Operation(summary = "Get products by category and price range", description = "Retrieve a list of products by category and price range")
    @ApiResponse(responseCode = "200", description = "List of products by category and price range")
    @ApiResponse(responseCode = "404", description = "Products not found")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public List<ProductModel> getProductsByCategoryAndPriceRange(@PathVariable String category, @PathVariable double minPrice, @PathVariable double maxPrice) {
        return productService.getProductsByCategoryAndPriceRange(category, minPrice, maxPrice);
    }

    @GetMapping("/products/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a product by its ID")
    @ApiResponse(responseCode = "200", description = "Product found")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public Optional<ProductModel> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // Create product endpoint can be added here if needed
    @PostMapping("/products")
    @Operation(summary = "Create a new product", description = "Create a new product")
    @ApiResponse(responseCode = "201", description = "Product created successfully")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public ProductModel createProduct(@RequestBody ProductModel product) {
        // validate the product object here if needed

        // For example, check if product name is not empty
        if (product.getProductName() == null || product.getProductName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        return productService.createProduct(product);
    }

    
    @DeleteMapping("/products/{id}")
    @Operation(summary = "Delete a product", description = "Delete a product by its ID")
    @ApiResponse(responseCode = "200", description = "Product deleted successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public void deleteProduct(@PathVariable UUID uuid) {
        // validate the product ID here if needed

        // For example, check if product ID is not null
        if (uuid == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        productService.deleteProduct(uuid);
    }

    // Update product endpoint can be added here if needed
    // Delete product endpoint can be added here if needed
    // Additional endpoints can be added here as needed
    // Example: Update product


}
