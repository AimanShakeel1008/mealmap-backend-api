package com.mealmap.mealmap_backend_api.respositories;

import com.mealmap.mealmap_backend_api.entities.MenuItem;
import com.mealmap.mealmap_backend_api.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    @Query("SELECT DISTINCT m.restaurant FROM MenuItem mi JOIN mi.menu m WHERE mi.name LIKE %:dishName%")
    List<Restaurant> findRestaurantsByDishName(@Param("dishName") String dishName);
}
