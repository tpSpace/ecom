package com.example.ecommerce.repository;

import java.util.List;
import java.util.UUID;


import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce.model.RatingModel;

public interface RatingRepository extends JpaRepository<RatingModel, UUID> {

    // panitgation 
    List<RatingModel> findByProductId(UUID productId);
    RatingModel findByUserId(UUID userId);
    Page<RatingModel> findByProductId(UUID productId, Pageable pageable);
    Page<RatingModel> findByRatingValue(int rating, Pageable pageable);
}
