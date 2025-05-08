package com.example.ecommerce.controller;

import com.example.ecommerce.dto.RatingRequest;
import com.example.ecommerce.dto.RatingResponse;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.service.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingControllerTest {

    @Mock
    private RatingService ratingService;

    @InjectMocks
    private RatingController ratingController;

    private UUID productId;
    private UUID userId;
    private RatingRequest validRequest;
    private RatingResponse response;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        userId = UUID.randomUUID();
        
        validRequest = new RatingRequest();
        validRequest.setProductId(productId);
        validRequest.setUserId(userId);
        validRequest.setScore(4);
        validRequest.setComment("Great product");

        response = new RatingResponse();
        response.setId(UUID.randomUUID());
        response.setProductId(productId);
        response.setUserId(userId);
        response.setScore(4);
        response.setComment("Great product");
    }

    @Test
    void getRatingsByProduct_ShouldReturnRatings() {
        // Arrange
        List<RatingResponse> expected = Collections.singletonList(response);
        when(ratingService.getRatingsByProduct(productId)).thenReturn(expected);

        // Act
        ResponseEntity<List<RatingResponse>> result = ratingController.getRatingsByProduct(productId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expected, result.getBody());
        verify(ratingService).getRatingsByProduct(productId);
    }

    @SuppressWarnings("NullAway")
    @Test
    void getRatingsByProduct_WhenNoRatings_ShouldReturnEmptyList() {
        // Arrange
        when(ratingService.getRatingsByProduct(productId)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<RatingResponse>> result = ratingController.getRatingsByProduct(productId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody(), "Response body should not be null");
        assertTrue(Objects.requireNonNullElse(result.getBody(), Collections.emptyList()).isEmpty());
    }

    @Test
    void addRating_WithValidRequest_ShouldReturnCreated() {
        // Arrange
        when(ratingService.createRating(validRequest)).thenReturn(response);

        // Act
        ResponseEntity<RatingResponse> result = ratingController.addRating(validRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(ratingService).createRating(validRequest);
    }

    @Test
    void addRating_WithInvalidScore_ShouldThrowException() {
        // Arrange
        RatingRequest invalidRequest = new RatingRequest();
        invalidRequest.setScore(6); // Invalid score
        when(ratingService.createRating(invalidRequest))
            .thenThrow(new IllegalArgumentException("Invalid rating value"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> ratingController.addRating(invalidRequest));
    }

    @Test
    void getAverageRating_ShouldReturnValue() {
        // Arrange
        double expectedAverage = 4.5;
        when(ratingService.getAverageRating(productId.toString())).thenReturn(expectedAverage);

        // Act
        ResponseEntity<Double> result = ratingController.getAverageRating(productId.toString());

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedAverage, result.getBody());
    }

    @Test
    void getAverageRating_ForNonExistentProduct_ShouldThrow() {
        // Arrange
        UUID invalidProductId = UUID.randomUUID();
        when(ratingService.getAverageRating(invalidProductId.toString()))
            .thenThrow(new ResourceNotFoundException("Product", "id", invalidProductId));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
            () -> ratingController.getAverageRating(invalidProductId.toString()));
    }
}