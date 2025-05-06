package com.example.ecommerce.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.ecommerce.model.CartItemModel;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemModel, UUID> {
    // deletes all CartItemModel records for a given CartModel ID
    void deleteByCart_Id(UUID cartId);
}