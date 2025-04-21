package com.example.ecommerce.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.model.RatingModel;
import com.example.ecommerce.service.RatingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/ratings")
@Tag(name = "Rating Controller", description = "Rating Management APIs")
public class RatingController {
    
    @Autowired
    private RatingService ratingService;
    
    @GetMapping
    @Operation(summary = "Get all ratings", description = "Returns a paginated list of all ratings")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved ratings")
    public ResponseEntity<Page<RatingModel>> getAllRatings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdOn") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? 
                Sort.Direction.ASC : Sort.Direction.DESC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        return ResponseEntity.ok(ratingService.getAllRatings(pageable));
    }
    
    @GetMapping("/product")
    @Operation(summary = "Get ratings by product", description = "Returns a paginated list of ratings for a product")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved ratings")
    public ResponseEntity<Page<RatingModel>> getRatingsByProduct(
            @RequestParam UUID productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ratingService.getRatingsByProduct(productId, pageable));
    }
    
    @GetMapping("/user")
    @Operation(summary = "Get ratings by user", description = "Returns a paginated list of ratings made by a user")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved ratings")
    public ResponseEntity<RatingModel> getRatingsByUser(
            @RequestParam UUID userId) {
        return ResponseEntity.ok(ratingService.getRatingsByUser(userId));
    }
    
    @GetMapping("/by-id")
    @Operation(summary = "Get rating by ID", description = "Returns a single rating by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved rating")
    @ApiResponse(responseCode = "404", description = "Rating not found")
    public ResponseEntity<RatingModel> getRatingById(@RequestParam UUID id) {
        return ratingService.getRatingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Create rating", description = "Creates a new product rating")
    @ApiResponse(responseCode = "201", description = "Rating created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid rating data")
    public ResponseEntity<RatingModel> createRating(@Valid @RequestBody RatingModel rating) {
        RatingModel createdRating = ratingService.saveRating(rating);
        return new ResponseEntity<>(createdRating, HttpStatus.CREATED);
    }
    
    @PutMapping
    @Operation(summary = "Update rating", description = "Updates an existing rating")
    @ApiResponse(responseCode = "200", description = "Rating updated successfully")
    @ApiResponse(responseCode = "404", description = "Rating not found")
    @ApiResponse(responseCode = "400", description = "Invalid rating data")
    public ResponseEntity<RatingModel> updateRating(
            @RequestParam UUID id,
            @Valid @RequestBody RatingModel rating) {
        
        return ratingService.getRatingById(id)
                .map(existingRating -> {
                    rating.setId(id);
                    RatingModel updatedRating = ratingService.saveRating(rating);
                    return ResponseEntity.ok(updatedRating);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping
    @Operation(summary = "Delete rating", description = "Deletes a rating by its ID")
    @ApiResponse(responseCode = "204", description = "Rating deleted successfully")
    @ApiResponse(responseCode = "404", description = "Rating not found")
    public ResponseEntity<Void> deleteRating(@RequestParam UUID id) {
        return ratingService.getRatingById(id)
                .map(rating -> {
                    ratingService.deleteRating(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}