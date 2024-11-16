package com.mealmap.mealmap_backend_api.controllers;

import com.mealmap.mealmap_backend_api.advices.ApiError;
import com.mealmap.mealmap_backend_api.dto.*;
import com.mealmap.mealmap_backend_api.entities.enums.PaymentMode;
import com.mealmap.mealmap_backend_api.services.CartService;
import com.mealmap.mealmap_backend_api.services.CustomerService;
import com.mealmap.mealmap_backend_api.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @Operation(summary = "Register a customer")
    ResponseEntity<UserDto> register(@Valid @RequestBody CustomerSignupDto customerSignupDto) {
        return new ResponseEntity<>(customerService.register(customerSignupDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_CUSTOMER")
    @GetMapping("/restaurants")
    @SecurityRequirement(name = "customerBearerAuth")
    @Operation(summary = "Retrieves list of all the available restaurants.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of all active restaurants retrieved successfully.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "[\n" +
                                    "    {\n" +
                                    "        \"id\": 1,\n" +
                                    "        \"name\": \"Barkaas\",\n" +
                                    "        \"address\": \"kapoorthala, Aliganj, Lucknow\",\n" +
                                    "        \"hoursOfOperation\": \"10 A.M. - 11 P.M.\",\n" +
                                    "        \"cuisineType\": \"Arabian\",\n" +
                                    "        \"contactNumber\": \"+91-8963930101\",\n" +
                                    "        \"open\": true,\n" +
                                    "        \"active\": true\n" +
                                    "    }\n" +
                                    "]")
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\n" +
                                    "    \"timeStamp\": \"2024-11-16T18:57:49.8294446\",\n" +
                                    "    \"data\": null,\n" +
                                    "    \"error\": {\n" +
                                    "        \"status\": \"INTERNAL_SERVER_ERROR\",\n" +
                                    "        \"message\": \"Access Denied\",\n" +
                                    "        \"subErrors\": null\n" +
                                    "    }\n" +
                                    "}")
                    )),

    })
    ResponseEntity<List<RestaurantDto>> getAllRestaurants() {
        return new ResponseEntity<>(customerService.getAllRestaurants(), HttpStatus.OK);
    }

    @Secured("ROLE_CUSTOMER")
    @SecurityRequirement(name = "customerBearerAuth")
    @GetMapping("/restaurants/{restaurantId}")
    @Operation(summary = "Retrieves Restaurant for a particular restaurantId")
    ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable Long restaurantId) {
        return new ResponseEntity<>(customerService.getRestaurantById(restaurantId), HttpStatus.OK);
    }

    @Secured("ROLE_CUSTOMER")
    @SecurityRequirement(name = "customerBearerAuth")
    @GetMapping("/restaurants/search")
    @Operation(summary = "Searches and retrieves restaurant of a particular name")
    ResponseEntity<List<RestaurantDto>> searchRestaurantByName(@RequestParam String restaurantName) {
        return new ResponseEntity<>(customerService.searchRestaurantByName(restaurantName), HttpStatus.OK);
    }

    @Secured("ROLE_CUSTOMER")
    @SecurityRequirement(name = "customerBearerAuth")
    @GetMapping("/restaurants/by-dish")
    @Operation(summary = "Searches and retrieves restaurants which serve a particular dish")
    ResponseEntity<List<RestaurantDto>> getRestaurantsByDishName(@RequestParam String disName) {
        return new ResponseEntity<>(customerService.getRestaurantsByDishName(disName), HttpStatus.OK);
    }

    @Secured("ROLE_CUSTOMER")
    @SecurityRequirement(name = "customerBearerAuth")
    @GetMapping("/restaurants/by-cuisine")
    @Operation(summary = "Searches and retrieves restaurants which serve a particular cuisine")
    ResponseEntity<List<RestaurantDto>> getRestaurantsByCuisine(@RequestParam String cuisineType) {
        return new ResponseEntity<>(customerService.getRestaurantsByCuisine(cuisineType), HttpStatus.OK);
    }

    @Secured("ROLE_CUSTOMER")
    @SecurityRequirement(name = "customerBearerAuth")
    @GetMapping("/restaurants/{restaurantId}/menu")
    @Operation(summary = "Retrieves menu of a particular restaurant")
    ResponseEntity<List<MenuDto>> getMenuForARestaurant(@PathVariable Long restaurantId) {
        return new ResponseEntity<>(customerService.getMenuForARestaurant(restaurantId), HttpStatus.OK);
    }

    @Secured("ROLE_CUSTOMER")
    @SecurityRequirement(name = "customerBearerAuth")
    @GetMapping("/restaurants/{restaurantId}/menu/{menuId}")
    @Operation(summary = "Retrieves a particular menu of a particular restaurant")
    ResponseEntity<MenuDto> getMenuForARestaurantById(@PathVariable Long restaurantId, @PathVariable Long menuId) {
        return new ResponseEntity<>(customerService.getMenuForARestaurantById(restaurantId,menuId), HttpStatus.OK);
    }

    @Secured("ROLE_CUSTOMER")
    @SecurityRequirement(name = "customerBearerAuth")
    @GetMapping("/restaurants/{restaurantId}/menu/{menuId}/menuItem/{menuItemId}")
    @Operation(summary = "Retrieves menu item of a particular restaurant")
    ResponseEntity<MenuItemDto> getMenuItemForARestaurantById(@PathVariable Long restaurantId, @PathVariable Long menuId, @PathVariable Long  menuItemId) {
        return new ResponseEntity<>(customerService.getMenuItemForARestaurantById(restaurantId, menuId, menuItemId), HttpStatus.OK);
    }

    @Secured("ROLE_CUSTOMER")
    @SecurityRequirement(name = "customerBearerAuth")
    @GetMapping("/cart")
    @Operation(summary = "Retrieves cart of the currently logged in customer")
    public ResponseEntity<CartDto> getCartOfACustomer() {
        CartDto cartDto = cartService.getCartOfACustomer();
        return ResponseEntity.ok(cartDto);
    }

    @Secured("ROLE_CUSTOMER")
    @SecurityRequirement(name = "customerBearerAuth")
    @PostMapping("/cart/{cartId}/addToCart")
    @Operation(summary = "Add item in the cart")
    public ResponseEntity<CartDto> addToCart(@PathVariable Long cartId, @RequestParam Long menuItemId, @RequestParam int quantity) {
        CartDto cartDto = cartService.addToCart(cartId, menuItemId, quantity);
        return ResponseEntity.ok(cartDto);
    }

    @Secured("ROLE_CUSTOMER")
    @SecurityRequirement(name = "customerBearerAuth")
    @PostMapping("/cart/{cartId}/updateQuantity")
    @Operation(summary = "Update quantity of the item in the cart")
    public ResponseEntity<CartDto> updateQuantityOfACartItem(@PathVariable Long cartId, @RequestParam Long cartItemId, @RequestParam int newQuantity) {
        CartDto cartDto = cartService.updateQuantityOfACartItem(cartId, cartItemId, newQuantity);
        return ResponseEntity.ok(cartDto);
    }

    @Secured("ROLE_CUSTOMER")
    @SecurityRequirement(name = "customerBearerAuth")
    @PostMapping("/order")
    @Operation(summary = "Place order of the items added in the cart")
    public ResponseEntity<OrderDto> placeOrder(@RequestParam PaymentMode paymentMode) {
        OrderDto order = orderService.placeOrder(paymentMode);
        return ResponseEntity.ok(order);
    }

    @Secured("ROLE_CUSTOMER")
    @SecurityRequirement(name = "customerBearerAuth")
    @GetMapping("/orders")
    @Operation(summary = "Retrieves all orders of a customer")
    public ResponseEntity<List<OrderDto>> getAllOrdersOfACustomer() {
        return new ResponseEntity<>(orderService.getAllOrdersOfACustomer(), HttpStatus.OK);
    }

    @Secured("ROLE_CUSTOMER")
    @SecurityRequirement(name = "customerBearerAuth")
    @GetMapping("/orders/{orderId}")
    @Operation(summary = "Retrieves a particular order of a customer")
    public ResponseEntity<OrderDto> getOrdersOfACustomerByOrderId(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.getOrdersOfACustomerByOrderId(orderId), HttpStatus.OK);
    }
}
