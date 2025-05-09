package com.example.ecommerce.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecommerce.dto.RatingRequest;
import com.example.ecommerce.dto.RatingResponse;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.mapper.RatingMapper;
import com.example.ecommerce.model.ProductModel;
import com.example.ecommerce.model.RatingModel;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.RatingRepository;
import com.example.ecommerce.repository.UserRepository;

@Service
public class RatingService {
    private static final Logger log = LoggerFactory.getLogger(RatingService.class);

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RatingMapper ratingMapper;

    public List<RatingResponse> getRatingsByProduct(UUID productId) {
        // Validate product exists
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product", "id", productId);
        }

        return ratingRepository.findByProductId(productId)
                .stream()
                .map(ratingMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public RatingResponse createRating(RatingRequest request) {
        // Validate product exists
        ProductModel product = productRepository.findById((request.getProductId()))
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.getProductId()));

        // Validate rating value (assuming 1-5 stars)
        if (request.getScore() < 1 || request.getScore() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5 stars");
        }

        // Find user
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));
        
        RatingModel rating;
        
        // Check if user already rated this product
        boolean ratingExists = ratingRepository.existsByProductIdAndUserId(product.getId(), request.getUserId());
        
        if (ratingExists) {
            // Find the existing rating (by using findAll and filtering)
            List<RatingModel> userRatings = ratingRepository.findByProductId(product.getId())
                    .stream()
                    .filter(r -> r.getUser().getId().equals(request.getUserId()))
                    .collect(Collectors.toList());
            
            // Update the existing rating
            rating = userRatings.get(0);
            rating.setScore(request.getScore());
            rating.setComment(request.getComment());
            log.info("Updated rating for product {} by user {}", product.getId(), request.getUserId());
        } else {
            // Create new rating
            rating = ratingMapper.toEntity(request);
            rating.setProduct(product);
            rating.setUser(user);
            log.info("Created rating for product {} by user {}", product.getId(), request.getUserId());
        }

        // Save rating
        rating = ratingRepository.save(rating);

        return ratingMapper.toResponseDto(rating);
    }

    public double getAverageRating(String productId) {
        // Validate product exists
        if (!productRepository.existsById(UUID.fromString(productId))) {
            throw new ResourceNotFoundException("Product", "id", productId);
        }

        Double averageRating = ratingRepository.findAverageRatingByProductId(UUID.fromString(productId));
        return averageRating != null ? averageRating : 0.0; // Return 0 if no ratings exist
    }
}