package com.matchamap.shop.controller;

import com.matchamap.shop.model.Shop;
import com.matchamap.shop.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController  // This handles HTTP requests
@RequestMapping("/api/shops")  // Base URL: /api/shops
@CrossOrigin(origins = "*")  // Allow frontend to call this (CORS)
public class ShopController {
    
    @Autowired
    private ShopService shopService;
    
    // GET /api/shops - Get all shops
    @GetMapping
    public List<Shop> getAllShops() {
        return shopService.getAllShops();
    }
    
    // GET /api/shops/1 - Get shop by ID
    @GetMapping("/{id}")
    public ResponseEntity<Shop> getShopById(@PathVariable Long id) {
        return shopService.getShopById(id)
            .map(ResponseEntity::ok)  // If found, return 200 OK
            .orElse(ResponseEntity.notFound().build());  // If not found, return 404
    }
    
    // POST /api/shops - Create new shop
    @PostMapping
    public ResponseEntity<Shop> createShop(@RequestBody Shop shop) {
        Shop created = shopService.createShop(shop);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    // PUT /api/shops/1 - Update shop
    @PutMapping("/{id}")
    public ResponseEntity<Shop> updateShop(@PathVariable Long id, @RequestBody Shop shop) {
        Shop updated = shopService.updateShop(id, shop);
        return ResponseEntity.ok(updated);
    }
    
    // DELETE /api/shops/1 - Delete shop
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShop(@PathVariable Long id) {
        shopService.deleteShop(id);
        return ResponseEntity.noContent().build();  // Return 204 No Content
    }
    
    // GET /api/shops/search?keyword=matcha - Search shops
    @GetMapping("/search")
    public List<Shop> searchShops(@RequestParam String keyword) {
        return shopService.searchShops(keyword);
    }
}