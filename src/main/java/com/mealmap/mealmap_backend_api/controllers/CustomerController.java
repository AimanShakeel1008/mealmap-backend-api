package com.mealmap.mealmap_backend_api.controllers;

import com.mealmap.mealmap_backend_api.dto.MenuDto;
import com.mealmap.mealmap_backend_api.dto.RestaurantDto;
import com.mealmap.mealmap_backend_api.dto.SignupDto;
import com.mealmap.mealmap_backend_api.dto.UserDto;
import com.mealmap.mealmap_backend_api.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    ResponseEntity<UserDto> register(@RequestBody SignupDto signupDto) {
        return new ResponseEntity<>(customerService.register(signupDto), HttpStatus.CREATED);
    }

    @GetMapping("/restaurants")
    ResponseEntity<List<RestaurantDto>> getAllRestaurants() {
        return new ResponseEntity<>(customerService.getAllRestaurants(), HttpStatus.OK);
    }

    @GetMapping("/restaurants/{restaurantId}")
    ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable Long restaurantId) {
        return new ResponseEntity<>(customerService.getRestaurantById(restaurantId), HttpStatus.OK);
    }

    @GetMapping("/restaurants/search")
    ResponseEntity<RestaurantDto> getRestaurantByName(@RequestParam String restaurantName) {
        return new ResponseEntity<>(customerService.getRestaurantByName(restaurantName), HttpStatus.OK);
    }

    @GetMapping("/restaurants/{restaurantId}/menu")
    ResponseEntity<MenuDto> getMenuForARestaurant(@PathVariable Long restaurantId) {
        return new ResponseEntity<>(customerService.getMenuForARestaurant(restaurantId), HttpStatus.OK);
    }

    @GetMapping("/restaurants/by-dish")
    public ResponseEntity<List<RestaurantDto>> getRestaurantsByDish(@RequestParam String dishName) {
        List<RestaurantDto> restaurants = customerService.getRestaurantsByDishName(dishName);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/restaurants/by-cuisine")
    public ResponseEntity<List<RestaurantDto>> getRestaurantsByCuisine(@RequestParam String cuisineType) {
        List<RestaurantDto> restaurants = customerService.getRestaurantsByCuisine(cuisineType);
        return ResponseEntity.ok(restaurants);
    }


}
