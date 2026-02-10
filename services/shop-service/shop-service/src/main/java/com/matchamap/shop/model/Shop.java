package com.matchamap.shop.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity  // This is a database table
@Table(name = "shops")
@Data  // Lombok: auto-generates getters/setters/toString
public class Shop {
    
    @Id  // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment
    private Long id;
    
    @Column(nullable = false)  // Required field
    private String name;
    
    private String address;
    
    @Column(nullable = false)
    private Double latitude;
    
    @Column(nullable = false)
    private Double longitude;
    
    @Column(length = 1000)  // Can hold 1000 characters
    private String description;
    
    private String phoneNumber;
    
    private String website;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist  // Runs before saving to database
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}