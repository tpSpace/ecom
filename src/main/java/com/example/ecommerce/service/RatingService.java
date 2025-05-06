package com.example.ecommerce.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.ecommerce.model.RatingModel;
import com.example.ecommerce.repository.RatingRepository;

@Service
public class RatingService {
    
    @Autowired
    private RatingRepository ratingRepository;
    
    public Page<RatingModel> getAllRatings(Pageable pageable) {
        return ratingRepository.findAll(pageable);
    }
    
    public Optional<RatingModel> getRatingById(UUID id) {
        return ratingRepository.findById(id);
    }
    
    public RatingModel saveRating(RatingModel rating) {
        return ratingRepository.save(rating);
    }
    
    public void deleteRating(UUID id) {
        ratingRepository.deleteById(id);
    }
    
    public Page<RatingModel> getRatingsByProduct(UUID productId, Pageable pageable) {
        return ratingRepository.findByProductId(productId, pageable);
    }

    public RatingModel getRatingsByUser(UUID userId) {
        return ratingRepository.findByUserId(userId);
    }
}