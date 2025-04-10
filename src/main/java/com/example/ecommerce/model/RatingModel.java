package com.example.ecommerce.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Entity()
@Table(name = "ratings")
@Data
public class RatingModel {
    
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToOne
    @JoinColumn(name = "product_id",table="product", nullable = false)
    private UUID productId;

    @Column(name = "rating_value", nullable = false)
    private double ratingValue;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @PrePersist
    private void onCreate() {
        createdOn = LocalDateTime.now();
    }
    
}