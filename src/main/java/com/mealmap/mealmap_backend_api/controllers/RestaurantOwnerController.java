package com.mealmap.mealmap_backend_api.controllers;

import com.mealmap.mealmap_backend_api.dto.*;
import com.mealmap.mealmap_backend_api.entities.enums.OrderStatus;
import com.mealmap.mealmap_backend_api.services.MenuService;
import com.mealmap.mealmap_backend_api.services.OrderService;
import com.mealmap.mealmap_backend_api.services.RestaurantOwnerService;
import com.mealmap.mealmap_backend_api.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurantOwner")
@RequiredArgsConstructor

public class RestaurantOwnerController {
    private final RestaurantOwnerService restaurantOwnerService;
    private final OrderService orderService;
    private final RestaurantService restaurantService;
    private final MenuService menuService;

    @PostMapping("/register")
    ResponseEntity<UserDto> register(@RequestBody RestaurantOwnerSignupDto restaurantOwnerSignupDto) {
        return new ResponseEntity<>(restaurantOwnerService.register(restaurantOwnerSignupDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @PostMapping("/restaurants")
    ResponseEntity<RestaurantDto> registerRestaurant(@RequestBody RestaurantDto restaurantDto) {
        return new ResponseEntity<>(restaurantService.createNewRestaurant(restaurantDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @GetMapping("/restaurants")
    ResponseEntity<List<RestaurantDto>> getAllRestaurants() {
        return new ResponseEntity<>(restaurantService.getAllRestaurants(), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @GetMapping("/restaurants/{restaurantId}")
    ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable Long restaurantId) {
        return new ResponseEntity<>(restaurantService.getRestaurantById(restaurantId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @GetMapping("/restaurants/search")
    ResponseEntity<RestaurantDto> getRestaurantByName(@RequestParam String restaurantName) {
        return new ResponseEntity<>(restaurantService.getRestaurantByName(restaurantName), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @PostMapping("/restaurants/{restaurantId}/menu")
    ResponseEntity<MenuDto> createMenuForARestaurant(@PathVariable Long restaurantId, @RequestBody MenuRequestDto menuRequestDto) {
        return new ResponseEntity<>(menuService.createMenuForARestaurant(restaurantId, menuRequestDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @GetMapping("/restaurants/{restaurantId}/menu")
    ResponseEntity<MenuDto> getMenuForARestaurant(@PathVariable Long restaurantId) {
        return new ResponseEntity<>(menuService.getMenuForARestaurant(restaurantId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @PutMapping("/order/{orderId}/accept")
    ResponseEntity<OrderDto> acceptOrder(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.updateOrderStatus(orderId), HttpStatus.OK);
    }
}
