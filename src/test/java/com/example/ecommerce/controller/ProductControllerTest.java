package com.example.ecommerce.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import com.example.ecommerce.dto.ProductResponse;
import com.example.ecommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;
    
    @Mock
    private ProductService productService;
    
    @InjectMocks
    private ProductController productController;

    private UUID testId;
    private ProductResponse response;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        testId = UUID.randomUUID();
        response = new ProductResponse();
        response.setId(testId);
    }

    @Test
    void createProduct_ValidRequest_ShouldReturnCreated() throws Exception {
        // Mock service
        when(productService.createProductWithImagesAndMap(any(), any()))
            .thenReturn(response);

        // Test & Verify
        mockMvc.perform(multipart("/api/v1/products")
                .file("images", "test".getBytes())
                .param("name", "Test Product")
                .param("price", "99.99")
                .param("quantity", "10")
                .param("category", testId.toString()))
            .andExpect(status().isCreated());
    }

    @Test
    void getProductById_ValidId_ShouldReturnProduct() throws Exception {
        // Mock service
        when(productService.getProductByIdAsDto(testId))
            .thenReturn(response);

        // Test & Verify
        mockMvc.perform(get("/api/v1/products/{id}", testId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(testId.toString()));
    }

    @Test
    void updateProduct_ValidRequest_ShouldReturnOk() throws Exception {
        // Mock service
        when(productService.updateProductWithImagesAndMap(eq(testId), any(), any()))
            .thenReturn(response);

        // Test & Verify
        mockMvc.perform(multipart("/api/v1/products/{id}", testId)
                .file("images", "test".getBytes())
                .param("name", "Updated Product")
                .param("price", "129.99")
                .param("quantity", "15")
                .param("category", testId.toString())
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                }))
            .andExpect(status().isOk());
    }

    @Test
    void deleteProduct_ValidId_ShouldReturnNoContent() throws Exception {
        // Test & Verify
        mockMvc.perform(delete("/api/v1/products")
                .param("id", testId.toString()))
            .andExpect(status().isNoContent());
    }
}