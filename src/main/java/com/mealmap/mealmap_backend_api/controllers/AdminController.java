package com.mealmap.mealmap_backend_api.controllers;

import com.mealmap.mealmap_backend_api.dto.*;
import com.mealmap.mealmap_backend_api.services.*;
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
    ResponseEntity<UserDto> register(@RequestBody AdminSignupDto adminSignupDto) {
        return new ResponseEntity<>(adminService.register(adminSignupDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/registerCustomer")
    ResponseEntity<UserDto> register(@RequestBody CustomerSignupDto customerSignupDto) {
        return new ResponseEntity<>(customerService.register(customerSignupDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/registerDeliveryPersonnel")
    ResponseEntity<UserDto> register(@RequestBody DeliveryPersonnelSignupDto deliveryPersonnelSignupDto) {
        return new ResponseEntity<>(deliveryPersonnelService.register(deliveryPersonnelSignupDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/registerRestaurantOwner")
    ResponseEntity<UserDto> register(@RequestBody RestaurantOwnerSignupDto restaurantOwnerSignupDto) {
        return new ResponseEntity<>(restaurantOwnerService.register(restaurantOwnerSignupDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/deactivateUser")
    ResponseEntity<UserDto> deactivateUser(@RequestParam String userEmail) {
        return new ResponseEntity<>(adminService.deactivateUser(userEmail), HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/activateUser")
    ResponseEntity<UserDto> activateUser(@RequestParam String userEmail) {
        return new ResponseEntity<>(adminService.activateUser(userEmail), HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDto> getOrderByOrderId(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.getOrderByOrderId(orderId), HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/orders/{orderId}/cancelOrder")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.cancelOrder(orderId), HttpStatus.OK);
    }


}
