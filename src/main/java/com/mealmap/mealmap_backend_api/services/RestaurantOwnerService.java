package com.mealmap.mealmap_backend_api.services;

import com.mealmap.mealmap_backend_api.dto.RestaurantOwnerSignupDto;
import com.mealmap.mealmap_backend_api.dto.UserDto;
import com.mealmap.mealmap_backend_api.entities.RestaurantOwner;
import com.mealmap.mealmap_backend_api.entities.User;

public interface RestaurantOwnerService {
    UserDto register(RestaurantOwnerSignupDto restaurantOwnerSignupDto);
    RestaurantOwner createNewRestaurantOwner(RestaurantOwnerSignupDto restaurantOwnerSignupDto, User user);

    RestaurantOwner getCurrentRestaurantOwner();
}
