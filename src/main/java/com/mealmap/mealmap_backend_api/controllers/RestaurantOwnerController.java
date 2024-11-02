package com.mealmap.mealmap_backend_api.controllers;

import com.mealmap.mealmap_backend_api.dto.*;
import com.mealmap.mealmap_backend_api.services.RestaurantOwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurantOwner")
@RequiredArgsConstructor
public class RestaurantOwnerController {
    private final RestaurantOwnerService restaurantOwnerService;

    @PostMapping("/register")
    ResponseEntity<UserDto> register(@RequestBody SignupDto signupDto) {
        return new ResponseEntity<>(restaurantOwnerService.register(signupDto), HttpStatus.CREATED);
    }

    @PostMapping("/restaurants")
    ResponseEntity<RestaurantDto> registerRestaurant(@RequestBody RestaurantDto restaurantDto) {
        return new ResponseEntity<>(restaurantOwnerService.registerRestaurant(restaurantDto), HttpStatus.CREATED);
    }

    @GetMapping("/restaurants")
    ResponseEntity<List<RestaurantDto>> getAllRestaurants() {
        return new ResponseEntity<>(restaurantOwnerService.getAllRestaurants(), HttpStatus.OK);
    }

    @GetMapping("/restaurants/{restaurantId}")
    ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable Long restaurantId) {
        return new ResponseEntity<>(restaurantOwnerService.getRestaurantById(restaurantId), HttpStatus.OK);
    }

    @GetMapping("/restaurants/search")
    ResponseEntity<RestaurantDto> getRestaurantByName(@RequestParam String restaurantName) {
        return new ResponseEntity<>(restaurantOwnerService.getRestaurantByName(restaurantName), HttpStatus.OK);
    }

    @PostMapping("/restaurants/{restaurantId}/menu")
    ResponseEntity<MenuDto> createMenuForARestaurant(@PathVariable Long restaurantId, @RequestBody MenuRequestDto menuRequestDto) {
        return new ResponseEntity<>(restaurantOwnerService.createMenuForARestaurant(restaurantId, menuRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("/restaurants/{restaurantId}/menu")
    ResponseEntity<MenuDto> getMenuForARestaurant(@PathVariable Long restaurantId) {
        return new ResponseEntity<>(restaurantOwnerService.getMenuForARestaurant(restaurantId), HttpStatus.OK);
    }
}
