package com.example.ecommerce.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;

import com.example.ecommerce.dto.CategoryRequest;
import com.example.ecommerce.dto.CategoryResponse;
import com.example.ecommerce.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private MockMvc mockMvc;
    
    @Mock private CategoryService categoryService;
    @InjectMocks private CategoryController categoryController;

    private UUID testId;
    private CategoryResponse response;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
        testId = UUID.randomUUID();
        response = new CategoryResponse();
        response.setId(testId);
        response.setName("Test Category");
    }

    @Test
    void createCategory_ValidRequest_ShouldReturnCreated() throws Exception {
        when(categoryService.createCategory(any(CategoryRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Test Category\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Test Category"));
    }

    @Test
    void getCategoryById_ValidId_ShouldReturnCategory() throws Exception {
        when(categoryService.getCategoryById(testId)).thenReturn(response);

        mockMvc.perform(get("/api/v1/categories/{id}", testId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(testId.toString()));
    }

    @Test
    void getAllCategories_ShouldReturnList() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/categories"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Test Category"));
    }

    @Test
    void updateCategory_ValidRequest_ShouldReturnOk() throws Exception {
        when(categoryService.updateCategory(eq(testId), any(CategoryRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/categories/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Updated Category\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Test Category"));
    }

    @Test
    void deleteCategory_ValidId_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/categories/{id}", testId))
            .andExpect(status().isNoContent());
    }
}