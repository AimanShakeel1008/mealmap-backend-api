package com.mealmap.mealmap_backend_api.services;

import com.mealmap.mealmap_backend_api.dto.CustomerSignupDto;
import com.mealmap.mealmap_backend_api.dto.MenuDto;
import com.mealmap.mealmap_backend_api.dto.RestaurantDto;
import com.mealmap.mealmap_backend_api.dto.UserDto;
import com.mealmap.mealmap_backend_api.entities.Customer;
import com.mealmap.mealmap_backend_api.entities.User;

import java.util.List;

public interface CustomerService {
    UserDto register(CustomerSignupDto customerSignupDto);
    Customer createNewCustomer(CustomerSignupDto customerSignupDto, User user);

    Customer getCurrentCustomer();

    List<RestaurantDto> getAllRestaurants();

    RestaurantDto getRestaurantById(Long restaurantId);

    RestaurantDto getRestaurantByName(String restaurantName);

    List<RestaurantDto> getRestaurantsByDishName(String dishName);

    List<RestaurantDto> getRestaurantsByCuisine(String cuisineType);
}
