package com.matchamap.shop.service;

import com.matchamap.shop.model.Shop;
import com.matchamap.shop.repository.ShopRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // generate contructor for final
@Slf4j // generate logger
@Transactional // db transactions are handled automatically if there's an error
public class ShopService {
    
    
    @Autowired  // Spring automatically provides the repository
    private final ShopRepository shopRepository;
    
    // Get all shops
    public List<Shop> getAllShops() {
        log.info("Getting all shops");
        return shopRepository.findAll();
    }
    
    // Get shop by ID
    public Optional<Shop> getShopById(Long id) {
        log.info("Getting shop with ID: " + id);
        return shopRepository.findById(id);
    }
    
    // Create new shop
    public Shop createShop(Shop shop) {
        log.info("Creating new shop: " + shop.getName() + " at address: " + shop.getAddress());
        // check if shop with same name and address already exists
        // there can be multiple shops with same name, but not at the same address 
        // i.e. chain stores are allowed, but not duplicate entries for the same location
        List<Shop> existingShops = getAllShops();
        for (Shop existingShop : existingShops) {
            if (existingShop.getName().equalsIgnoreCase(shop.getName()) &&
                existingShop.getAddress().equalsIgnoreCase(shop.getAddress())) {
                throw new RuntimeException("Shop with same name and address already exists");
            }
        }
        
        return shopRepository.save(shop);
    }
    
    /**
     * Update existing shop by ID
     * @param id
     * @param shopDetails
     * @return updated shop
     */
    public Shop updateShop(Long id, Shop shopDetails) {
        Shop shop = shopRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Shop not found"));
        
        shop.setName(shopDetails.getName());
        shop.setAddress(shopDetails.getAddress());
        shop.setCity(shopDetails.getCity());
        shop.setDescription(shopDetails.getDescription());
        shop.setPhoneNumber(shopDetails.getPhoneNumber());
        shop.setWebsite(shopDetails.getWebsite());
        
        return shopRepository.save(shop);
    }
    
    /**
     * Delete shop by ID
     * @param id
     */
    public void deleteShop(Long id) {
        log.info("Deleting shop with ID: " + id);

        if (!shopRepository.existsById(id)) {
            throw new RuntimeException("Shop not found");
        }   
        shopRepository.deleteById(id);
    }

    /**
     * Get shops by city
     * @param city
     * @return list of shops in the given city
     */
    public List<Shop> getShopsByCity(String city) {
        log.info("Getting shops in city: " + city);
        return shopRepository.findByCityIgnoreCase(city);
    }
    
    /**s
     * Search shops by name
     * @param keyword
     * @return list of shops matching the keyword
     */
    public List<Shop> searchShops(String keyword) {
        log.info("Getting shops with keyword: " + keyword);
        return shopRepository.findByNameContainingIgnoreCase(keyword);
    }

    /**
     * Get all unique cities that have at least one matcha shop
     * @return
     */
    public List<String> getAllCities() {
        log.info("Getting all cities with matcha shops");
        return shopRepository.findAllCities();
    }

    /**
     * Count the number of shops in a specific city
     * @param city
     * @return
     */
    public long countShopsInCity(String city) {
        log.info("Counting shops in city: " + city);
        return shopRepository.countByCityIgnoreCase(city);
    }
}