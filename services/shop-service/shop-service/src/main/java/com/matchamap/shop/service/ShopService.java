package com.matchamap.shop.service;

import com.matchamap.shop.model.Shop;
import com.matchamap.shop.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ShopService {
    
    @Autowired  // Spring automatically provides the repository
    private ShopRepository shopRepository;
    
    // Get all shops
    public List<Shop> getAllShops() {
        return shopRepository.findAll();
    }
    
    // Get shop by ID
    public Optional<Shop> getShopById(Long id) {
        return shopRepository.findById(id);
    }
    
    // Create new shop
    public Shop createShop(Shop shop) {
        // Could add validation here
        return shopRepository.save(shop);
    }
    
    // Update shop
    public Shop updateShop(Long id, Shop shopDetails) {
        Shop shop = shopRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Shop not found"));
        
        shop.setName(shopDetails.getName());
        shop.setAddress(shopDetails.getAddress());
        shop.setLatitude(shopDetails.getLatitude());
        shop.setLongitude(shopDetails.getLongitude());
        shop.setDescription(shopDetails.getDescription());
        shop.setPhoneNumber(shopDetails.getPhoneNumber());
        shop.setWebsite(shopDetails.getWebsite());
        
        return shopRepository.save(shop);
    }
    
    // Delete shop
    public void deleteShop(Long id) {
        shopRepository.deleteById(id);
    }
    
    // Search shops
    public List<Shop> searchShops(String keyword) {
        return shopRepository.findByNameContainingIgnoreCase(keyword);
    }
}