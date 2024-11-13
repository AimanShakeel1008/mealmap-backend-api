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
    ResponseEntity<List<MenuDto>> getMenuForARestaurant(@PathVariable Long restaurantId) {
        return new ResponseEntity<>(customerService.getMenuForARestaurant(restaurantId), HttpStatus.OK);
    }

    @Secured("ROLE_CUSTOMER")
    @GetMapping("/restaurants/{restaurantId}/menu/{menuId}")
    ResponseEntity<MenuDto> getMenuForARestaurantById(@PathVariable Long restaurantId, @PathVariable Long menuId) {
        return new ResponseEntity<>(customerService.getMenuForARestaurantById(restaurantId,menuId), HttpStatus.OK);
    }

    @Secured("ROLE_CUSTOMER")
    @GetMapping("/restaurants/{restaurantId}/menu/{menuId}/menuItem/{menuItemId}")
    ResponseEntity<MenuItemDto> getMenuItemForARestaurantById(@PathVariable Long restaurantId, @PathVariable Long menuId, @PathVariable Long  menuItemId) {
        return new ResponseEntity<>(customerService.getMenuItemForARestaurantById(restaurantId, menuId, menuItemId), HttpStatus.OK);
    }

    @Secured("ROLE_CUSTOMER")
    @GetMapping("/cart")
    public ResponseEntity<CartDto> getCartOfACustomer() {
        CartDto cartDto = cartService.getCartOfACustomer();
        return ResponseEntity.ok(cartDto);
    }

    @Secured("ROLE_CUSTOMER")
    @PostMapping("/cart/{cartId}/addToCart")
    public ResponseEntity<CartDto> addToCart(@PathVariable Long cartId, @RequestParam Long menuItemId, @RequestParam int quantity) {
        CartDto cartDto = cartService.addToCart(cartId, menuItemId, quantity);
        return ResponseEntity.ok(cartDto);
    }

    @Secured("ROLE_CUSTOMER")
    @PostMapping("/cart/{cartId}/updateQuantity")
    public ResponseEntity<CartDto> updateQuantityOfACartItem(@PathVariable Long cartId, @RequestParam Long cartItemId, @RequestParam int newQuantity) {
        CartDto cartDto = cartService.updateQuantityOfACartItem(cartId, cartItemId, newQuantity);
        return ResponseEntity.ok(cartDto);
    }

    @Secured("ROLE_CUSTOMER")
    @PostMapping("/order")
    public ResponseEntity<OrderDto> placeOrder(@RequestParam PaymentMode paymentMode) {
        OrderDto order = orderService.placeOrder(paymentMode);
        return ResponseEntity.ok(order);
    }

    @Secured("ROLE_CUSTOMER")
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> getAllOrdersOfACustomer() {
        return new ResponseEntity<>(orderService.getAllOrdersOfACustomer(), HttpStatus.OK);
    }

    @Secured("ROLE_CUSTOMER")
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDto> getOrdersOfACustomerByOrderId(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.getOrdersOfACustomerByOrderId(orderId), HttpStatus.OK);
    }
}
