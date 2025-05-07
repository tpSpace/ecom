package com.example.ecommerce.mapper;

import org.springframework.stereotype.Component;

import com.example.ecommerce.dto.RatingRequest;
import com.example.ecommerce.dto.RatingResponse;
import com.example.ecommerce.model.RatingModel;

@Component
public class RatingMapper {

    public RatingResponse toDto(RatingModel entity) {
        if (entity == null)
            return null;

        RatingResponse dto = new RatingResponse();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setProductName(entity.getProduct().getName());

        // Set image URL if available
        if (entity.getProduct().getProductImages() != null &&
                !entity.getProduct().getProductImages().isEmpty()) {
            dto.setProductImageUrl("/api/v1/products/images/" +
                    entity.getProduct().getProductImages().get(0).getId());
        }

        dto.setUsername(entity.getUser().getFirstName());
        dto.setScore(entity.getScore());
        dto.setComment(entity.getComment());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    public RatingModel toEntity(RatingRequest req) {
        if (req == null)
            return null;

        RatingModel entity = new RatingModel();
        entity.setScore(req.getScore());
        entity.setComment(req.getComment());

        // Set product and user entities if needed
        // entity.setProduct(product);
        // entity.setUser(user);

        return entity;
    }

    public void updateEntity(RatingModel existing, RatingRequest req) {
        if (req == null)
            return;

        existing.setScore(req.getScore());
        existing.setComment(req.getComment());

        // Update product and user entities if needed
        // existing.setProduct(product);
        // existing.setUser(user);
    }
}
