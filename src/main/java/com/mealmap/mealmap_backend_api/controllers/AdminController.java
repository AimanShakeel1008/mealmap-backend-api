package com.mealmap.mealmap_backend_api.controllers;

import com.mealmap.mealmap_backend_api.dto.*;
import com.mealmap.mealmap_backend_api.services.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final CustomerService customerService;
    private final DeliveryPersonnelService deliveryPersonnelService;
    private final RestaurantOwnerService restaurantOwnerService;
    private final AdminService adminService;
    private final OrderService orderService;

    @PostMapping("/register")
    @Operation(summary = "Register an Admin")
    ResponseEntity<UserDto> register(@RequestBody AdminSignupDto adminSignupDto) {
        return new ResponseEntity<>(adminService.register(adminSignupDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "adminBearerAuth")
    @PostMapping("/registerCustomer")
    @Operation(summary = "Register an Customer by Admin")
    ResponseEntity<UserDto> register(@RequestBody CustomerSignupDto customerSignupDto) {
        return new ResponseEntity<>(customerService.register(customerSignupDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "adminBearerAuth")
    @PostMapping("/registerDeliveryPersonnel")
    @Operation(summary = "Register a Delivery Personnel by Admin")
    ResponseEntity<UserDto> register(@RequestBody DeliveryPersonnelSignupDto deliveryPersonnelSignupDto) {
        return new ResponseEntity<>(deliveryPersonnelService.register(deliveryPersonnelSignupDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "adminBearerAuth")
    @PostMapping("/registerRestaurantOwner")
    @Operation(summary = "Register a Restaurant Owner by Admin")
    ResponseEntity<UserDto> register(@RequestBody RestaurantOwnerSignupDto restaurantOwnerSignupDto) {
        return new ResponseEntity<>(restaurantOwnerService.register(restaurantOwnerSignupDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "adminBearerAuth")
    @PutMapping("/deactivateUser")
    @Operation(summary = "Deactivate an user by Admin")
    ResponseEntity<UserDto> deactivateUser(@RequestParam String userEmail) {
        return new ResponseEntity<>(adminService.deactivateUser(userEmail), HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "adminBearerAuth")
    @PutMapping("/activateUser")
    @Operation(summary = "Activate an user by Admin")
    ResponseEntity<UserDto> activateUser(@RequestParam String userEmail) {
        return new ResponseEntity<>(adminService.activateUser(userEmail), HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "adminBearerAuth")
    @GetMapping("/orders")
    @Operation(summary = "Retrieve list of all orders by Admin")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "adminBearerAuth")
    @GetMapping("/orders/{orderId}")
    @Operation(summary = "Retrieve a particular order by Admin")
    public ResponseEntity<OrderDto> getOrderByOrderId(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.getOrderByOrderId(orderId), HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @SecurityRequirement(name = "adminBearerAuth")
    @PutMapping("/orders/{orderId}/cancelOrder")
    @Operation(summary = "Cancel an order by Admin")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.cancelOrder(orderId), HttpStatus.OK);
    }

}
