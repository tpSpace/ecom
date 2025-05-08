package com.example.ecommerce.controller;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.dto.RatingRequest;
import com.example.ecommerce.dto.RatingResponse;
import com.example.ecommerce.service.RatingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/ratings")
@Tag(name = "RatingController", description = "Product Ratings Management")
public class RatingController {
    private static final Logger log = LoggerFactory.getLogger(RatingController.class);

    @Autowired
    private RatingService ratingService;

    @GetMapping("/{productId}")
    @Operation(summary = "Get ratings for a product", description = "Retrieve all ratings for a specific product")
    @ApiResponse(responseCode = "200", description = "List of ratings")
    public ResponseEntity<List<RatingResponse>> getRatingsByProduct(@PathVariable UUID productId) {
        log.info("Getting ratings for product ID: {}", productId);
        List<RatingResponse> ratings = ratingService.getRatingsByProduct(productId);
        log.info("Retrieved {} ratings for product ID: {}", ratings.size(), productId);
        return ResponseEntity.ok(ratings);
    }

    @PostMapping
    @Operation(summary = "Add rating to a product", description = "Add a rating and comment for a product")
    @ApiResponse(responseCode = "201", description = "Rating created successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized - User must be logged in")
    @ApiResponse(responseCode = "400", description = "Invalid rating data")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<RatingResponse> addRating(
            @Valid @RequestBody RatingRequest ratingRequest) {

        log.info("Request to add rating for product ID: {}", ratingRequest.getProductId());
        RatingResponse createdRating = ratingService.createRating(ratingRequest);
        return new ResponseEntity<>(createdRating, HttpStatus.CREATED);
    }
}