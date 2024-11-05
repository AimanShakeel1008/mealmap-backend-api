package com.mealmap.mealmap_backend_api.services;

import com.mealmap.mealmap_backend_api.dto.*;
import com.mealmap.mealmap_backend_api.entities.DeliveryPersonnel;
import com.mealmap.mealmap_backend_api.entities.RestaurantOwner;
import com.mealmap.mealmap_backend_api.entities.User;

import java.util.List;

public interface RestaurantOwnerService {
    UserDto register(RestaurantOwnerSignupDto restaurantOwnerSignupDto);
    RestaurantOwner createNewRestaurantOwner(RestaurantOwnerSignupDto restaurantOwnerSignupDto, User user);

    RestaurantOwner getCurrentRestaurantOwner();
}
