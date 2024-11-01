package com.mealmap.mealmap_backend_api.services;

import com.mealmap.mealmap_backend_api.dto.RestaurantDto;

public interface RestaurantService {
    RestaurantDto createNewRestaurant(RestaurantDto restaurantDto);
}
