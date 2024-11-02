package com.mealmap.mealmap_backend_api.services;

import com.mealmap.mealmap_backend_api.dto.RestaurantDto;
import com.mealmap.mealmap_backend_api.dto.SignupDto;
import com.mealmap.mealmap_backend_api.dto.UserDto;
import com.mealmap.mealmap_backend_api.entities.RestaurantOwner;
import com.mealmap.mealmap_backend_api.entities.User;

import java.util.List;

public interface RestaurantOwnerService {
    UserDto register(SignupDto signupDto);

    RestaurantOwner createNewRestaurantOwner(User user);

    RestaurantDto registerRestaurant(RestaurantDto restaurantDto);

    RestaurantDto getRestaurantById(Long restaurantId);

    RestaurantDto getRestaurantByName(String restaurantName);

    List<RestaurantDto> getAllRestaurants();
}
