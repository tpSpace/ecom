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
import com.example.ecommerce.exception.DuplicateResourceException;
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

        // check user

        // Validate rating value (assuming 1-5 stars)
        if (request.getScore() < 1 || request.getScore() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5 stars");
        }

        // Check if user already rated this product
        if (ratingRepository.existsByProductIdAndUserId(product.getId(), request.getUserId())) {
            throw new DuplicateResourceException("Rating", "user-product pair",
                    "User has already rated this product");
        }

        // Create rating
        RatingModel rating = ratingMapper.toEntity(request);
        rating.setProduct(product);
        rating.setUser(userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId())));

        // Save rating
        rating = ratingRepository.save(rating);
        log.info("Created rating for product {} by user {}", product.getId(), request.getUserId());

        return ratingMapper.toResponseDto(rating);
    }
}