package com.mealmap.mealmap_backend_api.controllers;

import com.mealmap.mealmap_backend_api.dto.*;
import com.mealmap.mealmap_backend_api.entities.enums.PaymentMode;
import com.mealmap.mealmap_backend_api.services.CartService;
import com.mealmap.mealmap_backend_api.services.CustomerService;
import com.mealmap.mealmap_backend_api.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
@Secured("ROLE_CUSTOMER")
public class CustomerController {

    private final CustomerService customerService;
    private final CartService cartService;
    private final OrderService orderService;

    @PostMapping("/register")
    ResponseEntity<UserDto> register(@RequestBody CustomerSignupDto customerSignupDto) {
        return new ResponseEntity<>(customerService.register(customerSignupDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_CUSTOMER")
    @GetMapping("/restaurants")
    ResponseEntity<List<RestaurantDto>> getAllRestaurants() {
        return new ResponseEntity<>(customerService.getAllRestaurants(), HttpStatus.OK);
    }

    @Secured("ROLE_CUSTOMER")
    @GetMapping("/restaurants/{restaurantId}")
    ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable Long restaurantId) {
        return new ResponseEntity<>(customerService.getRestaurantById(restaurantId), HttpStatus.OK);
    }

    @Secured("ROLE_CUSTOMER")
    @GetMapping("/restaurants/search")
    ResponseEntity<RestaurantDto> getRestaurantByName(@RequestParam String restaurantName) {
        return new ResponseEntity<>(customerService.getRestaurantByName(restaurantName), HttpStatus.OK);
    }

    @Secured("ROLE_CUSTOMER")
    @GetMapping("/restaurants/{restaurantId}/menu")
    ResponseEntity<MenuDto> getMenuForARestaurant(@PathVariable Long restaurantId) {
        return new ResponseEntity<>(customerService.getMenuForARestaurant(restaurantId), HttpStatus.OK);
    }

    @Secured("ROLE_CUSTOMER")
    @GetMapping("/restaurants/by-dish")
    public ResponseEntity<List<RestaurantDto>> getRestaurantsByDish(@RequestParam String dishName) {
        List<RestaurantDto> restaurants = customerService.getRestaurantsByDishName(dishName);
        return ResponseEntity.ok(restaurants);
    }

    @Secured("ROLE_CUSTOMER")
    @GetMapping("/restaurants/by-cuisine")
    public ResponseEntity<List<RestaurantDto>> getRestaurantsByCuisine(@RequestParam String cuisineType) {
        List<RestaurantDto> restaurants = customerService.getRestaurantsByCuisine(cuisineType);
        return ResponseEntity.ok(restaurants);
    }

    @Secured("ROLE_CUSTOMER")
    @PostMapping("/cart")
    public ResponseEntity<CartDto> addToCart(@RequestParam Long menuItemId, @RequestParam int quantity) {
        CartDto cartDto = cartService.addToCart(menuItemId, quantity);
        return ResponseEntity.ok(cartDto);
    }

    @Secured("ROLE_CUSTOMER")
    @PostMapping("/order")
    public ResponseEntity<OrderDto> placeOrder(@RequestParam PaymentMode paymentMode) {
        OrderDto order = orderService.placeOrder(paymentMode);
        return ResponseEntity.ok(order);
    }
}
