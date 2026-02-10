package com.matchamap.shop.repository;

import com.matchamap.shop.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    
    // Spring automatically implements these!
    
    // Find shops by name (case-insensitive)
    List<Shop> findByNameContainingIgnoreCase(String name);
    
    // Find shops near a location (we'll implement this manually later)
    // For now, this is just a placeholder
}