package com.matchamap.shop.repository;

import com.matchamap.shop.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    
    // Spring automatically implements these!
    
    /**
     * Find shops by name containing the given keyword, ignoring case.
     * @param name
     * @return list of shops matching given keyword
     */
    List<Shop> findByNameContainingIgnoreCase(String keyword);
    
    /**
     * Find shops in a specific city, ignore case
     * @param city
     * @return list of shops matching given city
     */
    List<Shop> findByCityIgnoreCase(String city);

    /**
     * get all unique cities that have at least one matcha shop
     * @return list of unique cities with matcha shops
     */
    @Query("SELECT DISTINCT s.city FROM Shop s ORDER BY s.city ASC")
    List<String> findAllCities();

    /**
     * Find shops by name containing the given keyword and city, ignoring case.
     * @param keyword
     * @param city
     * @return list of shops matching given keyword
     */
    List<Shop> findByNameContainingIgnoreCaseAndCityIgnoreCase(String keyword, String city);

    /**
     * Count the number of shops in a specific city
     * @param city
     * @return number of shops in the given city
     */
    long countByCityIgnoreCase(String city);

}