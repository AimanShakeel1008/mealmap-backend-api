package com.mealmap.mealmap_backend_api.services;

import com.mealmap.mealmap_backend_api.dto.*;
import com.mealmap.mealmap_backend_api.entities.Customer;
import com.mealmap.mealmap_backend_api.entities.User;

import java.util.List;

public interface CustomerService {
    UserDto register(CustomerSignupDto customerSignupDto);
    Customer createNewCustomer(CustomerSignupDto customerSignupDto, User user);

    Customer getCurrentCustomer();

    List<RestaurantDto> getAllRestaurants();

    RestaurantDto getRestaurantById(Long restaurantId);

    List<RestaurantDto> searchRestaurantByName(String restaurantName);

    List<RestaurantDto> getRestaurantsByDishName(String dishName);

    List<RestaurantDto> getRestaurantsByCuisine(String cuisineType);

    List<MenuDto> getMenuForARestaurant(Long restaurantId);

    MenuDto getMenuForARestaurantById(Long restaurantId, Long menuId);

    MenuItemDto getMenuItemForARestaurantById(Long restaurantId, Long menuId, Long menuItemId);
}
