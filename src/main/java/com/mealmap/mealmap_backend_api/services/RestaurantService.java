package com.mealmap.mealmap_backend_api.services;


import com.mealmap.mealmap_backend_api.dto.RestaurantDto;
import com.mealmap.mealmap_backend_api.entities.RestaurantOwner;

import java.util.List;

public interface RestaurantService {
    RestaurantDto createNewRestaurant(RestaurantDto restaurantDto);

    RestaurantDto getRestaurantById(Long restaurantId);

    RestaurantDto getRestaurantByName(String restaurantName);

    List<RestaurantDto> getAllRestaurants();

    List<RestaurantDto> getAllRestaurantsOfAnOwner(RestaurantOwner restaurantOwner);

    List<RestaurantDto> getRestaurantsByCuisine(String cuisineType);
}
