package com.mealmap.mealmap_backend_api.respositories;

import com.mealmap.mealmap_backend_api.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByName(String name);

    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.cuisineType) LIKE LOWER(CONCAT('%', :cuisineType, '%'))")
    Optional<Restaurant> findRestaurantByCuisineType(@Param("cuisineType") String cuisineType);
}
