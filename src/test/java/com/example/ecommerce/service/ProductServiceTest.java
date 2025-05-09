package com.example.ecommerce.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.ecommerce.dto.ProductRequest;
import com.example.ecommerce.dto.ProductResponse;
import com.example.ecommerce.exception.DuplicateResourceException;
import com.example.ecommerce.model.CategoryModel;
import com.example.ecommerce.model.ProductModel;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductImageRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock private ProductRepository productRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private ProductImageRepository productImageRepository;
    @Mock private ProductMapper mapper;
    @Mock private VectorSearchService vectorSearchService;
    @InjectMocks private ProductService productService;

    private UUID testId;
    private ProductModel product;
    private ProductRequest request;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        product = new ProductModel();
        product.setId(testId);
        
        request = new ProductRequest();
        request.setName("Test Product");
        request.setPrice(99.99);
        request.setQuantity(10);
        request.setCategory(testId.toString());
    }

    @Test
    void createProductWithImages_ValidRequest_ShouldSaveProduct() {
        // Mock dependencies
        when(categoryRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(new CategoryModel()));
        when(mapper.toEntity(any(ProductRequest.class)))
            .thenReturn(new ProductModel());
        when(productRepository.save(any(ProductModel.class)))
            .thenReturn(product);
        when(mapper.toResponseDto(any(ProductModel.class)))
            .thenReturn(new ProductResponse());

        // Test
        ProductResponse response = productService.createProductWithImagesAndMap(request, null);

        // Verify
        assertNotNull(response);
        verify(productRepository).save(any(ProductModel.class));
    }

    @Test
    void createProductWithImages_DuplicateSku_ShouldThrowException() {
        // Setup
        request.setSku("DUPLICATE-123");
        when(productRepository.existsBySku(anyString()))
            .thenReturn(true);

        // Test & Verify
        assertThrows(DuplicateResourceException.class, () -> {
            productService.createProductWithImagesAndMap(request, null);
        });
    }

    @Test
    void updateProductWithImages_ValidRequest_ShouldUpdateProduct() {
        // Mock dependencies
        when(productRepository.findById(testId))
            .thenReturn(Optional.of(product));
        when(categoryRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(new CategoryModel()));
        when(productRepository.save(any(ProductModel.class)))
            .thenReturn(product);
        when(mapper.toResponseDto(any(ProductModel.class)))
            .thenReturn(new ProductResponse());

        // Test
        ProductResponse response = productService.updateProductWithImagesAndMap(testId, request, null);

        // Verify
        assertNotNull(response);
        verify(productRepository).save(any(ProductModel.class));
    }

    @Test
    void searchProducts_WithFilters_ShouldReturnPage() {
        // Mock dependencies for the productRepository.findAll path
        Page<ProductModel> mockProductModelPage = new PageImpl<>(List.of(product), PageRequest.of(0, 10), 1);
        when(productRepository.findAll(
            (Specification<ProductModel>) any(),
            (Pageable) any()
        )).thenReturn(mockProductModelPage);

        // Mock the mapper call that will be used on the results of productRepository.findAll
        when(mapper.toResponseDto(any(ProductModel.class)))
            .thenReturn(new ProductResponse());

        // The VectorSearchService is not called by the current ProductService.searchProducts method,
        // so its mock is not needed for this specific test case.

        // Test
        Page<ProductResponse> result = productService.searchProducts(
            0, 10, "test", testId.toString(), 50.0, 100.0, "name", "asc");

        // Verify
        assertNotNull(result); 
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void deleteProductWithValidation_ExistingProduct_ShouldDelete() {
        // Mock dependencies
        when(productRepository.findById(testId))
            .thenReturn(Optional.of(product));

        // Test
        productService.deleteProductWithValidation(testId);

        // Verify
        verify(productRepository).delete(product);
    }
}