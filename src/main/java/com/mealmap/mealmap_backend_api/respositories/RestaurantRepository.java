package com.mealmap.mealmap_backend_api.respositories;

import com.mealmap.mealmap_backend_api.entities.Restaurant;
import com.mealmap.mealmap_backend_api.entities.RestaurantOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findByName(@Param("name") String name);

    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Restaurant> searchByName(@Param("name") String name);

    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.cuisineType) LIKE LOWER(CONCAT('%', :cuisineType, '%'))")
    Optional<Restaurant> findRestaurantByCuisineType(@Param("cuisineType") String cuisineType);

    List<Restaurant> findByOwner(RestaurantOwner restaurantOwner);
}
