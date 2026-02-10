package com.matchamap.shop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity  // This is a database table
@Table(name = "shops")
@Data  // Lombok: auto-generates getters/setters/toString
public class Shop {
    
    @Id  // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment
    private Long id;
    
    @NotBlank(message = "Shop name is required")  // Validation: must not be blank
    @Size(min = 2, max = 255, message = "Shop name must be between 2 and 255 characters")  // Validation: max length
    @Column(nullable = false, length = 255)  // Required field
    private String name;
    
    @NotBlank(message = "Address name is required")
    @Size(max = 255, message = "Address must be at most 255 characters")
    @Column(nullable = false, length = 255)  // Required field
    private String address;
    
    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must be at most 100 characters")
    @Column(nullable = false, length = 100)  // require field & Max length of 100 characters
    private String city;

    @Column(length = 1000)  // Can hold 1000 characters
    private String description;

    @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$", message = "Phone number must be in format: 123-456-7890")
    @Column(name = "phone_number", length = 12)
    private String phoneNumber;
    
    @Size(max = 255, message = "Website URL must not exceed 255 characters")
    @Column(length = 255)
    private String website;
    
    @Column(name = "created_at", updatable = false)  // Set once, never updated
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist  // Runs before saving to database
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate  // Runs before updating existing record
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}