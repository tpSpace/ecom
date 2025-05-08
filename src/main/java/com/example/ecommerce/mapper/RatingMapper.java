package com.example.ecommerce.mapper;

import org.springframework.stereotype.Component;
import com.example.ecommerce.dto.RatingRequest;
import com.example.ecommerce.dto.RatingResponse;
import com.example.ecommerce.model.RatingModel;

@Component
public class RatingMapper {

    public RatingResponse toDto(RatingModel entity) {
        if (entity == null) {
            return null;
        }

        RatingResponse dto = new RatingResponse();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setUserId(entity.getUser().getId());
        dto.setScore(entity.getScore());
        dto.setComment(entity.getComment());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    public RatingModel toEntity(RatingRequest req) {
        if (req == null) {
            return null;
        }

        RatingModel entity = new RatingModel();
        entity.setScore(req.getScore());
        entity.setComment(req.getComment());

        return entity;
    }

    public void updateEntity(RatingModel existing, RatingRequest req) {
        if (req == null) {
            return;
        }

        existing.setScore(req.getScore());
        existing.setComment(req.getComment());
    }

    public RatingResponse toResponseDto(RatingModel rating) {
        if (rating == null) {
            return null;
        }

        RatingResponse response = new RatingResponse();
        response.setId(rating.getId());
        response.setProductId(rating.getProduct().getId());
        response.setUserId(rating.getUser().getId());
        response.setScore(rating.getScore());
        response.setComment(rating.getComment());
        response.setCreatedAt(rating.getCreatedAt());
        return response;
    }
}