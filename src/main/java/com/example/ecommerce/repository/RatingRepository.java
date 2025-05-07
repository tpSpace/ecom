package com.example.ecommerce.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.model.RatingModel;

@Repository
public interface RatingRepository extends JpaRepository<RatingModel, UUID> {

    Page<RatingModel> findByProductId(UUID productId, Pageable pageable);

    Page<RatingModel> findByUserId(UUID userId, Pageable pageable);

    boolean existsByUserIdAndProductId(UUID userId, UUID productId);

    @Query("SELECT AVG(r.score) FROM RatingModel r WHERE r.product.id = :productId")
    Double findAverageRatingByProductId(UUID productId);

    int deleteByProductId(UUID productId);
}