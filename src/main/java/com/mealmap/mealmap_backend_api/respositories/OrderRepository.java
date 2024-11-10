package com.mealmap.mealmap_backend_api.respositories;

import com.mealmap.mealmap_backend_api.entities.Order;
import com.mealmap.mealmap_backend_api.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByRestaurant(Restaurant restaurant);
}
