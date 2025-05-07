package com.example.ecommerce.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Entity
@Table(name = "ratings")
@Data
public class RatingModel {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user; // Changed from UUID userId

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductModel product;

    @Column(name = "score", nullable = false)
    private double score; // 1-5

    @Column(name = "comment", length = 255)
    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PreUpdate
    private void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @PrePersist
    private void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Integer getScore() {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("Rating value must be between 1 and 5");
        }
        return (int) Math.round(score);
    }

    public void setScore(Integer score) {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("Rating value must be between 1 and 5");
        }
        this.score = score;
    }

}