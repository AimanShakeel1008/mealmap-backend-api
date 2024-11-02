package com.mealmap.mealmap_backend_api.services;

import com.mealmap.mealmap_backend_api.dto.RestaurantDto;

import java.util.List;

public interface MenuItemService {
    List<RestaurantDto> getRestaurantsByDishName(String dishName);
}
