package com.mealmap.mealmap_backend_api.controllers;

import com.mealmap.mealmap_backend_api.dto.*;
import com.mealmap.mealmap_backend_api.entities.RestaurantOwner;
import com.mealmap.mealmap_backend_api.services.MenuService;
import com.mealmap.mealmap_backend_api.services.OrderService;
import com.mealmap.mealmap_backend_api.services.RestaurantOwnerService;
import com.mealmap.mealmap_backend_api.services.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @Operation(summary = "Register a restaurant owner")
    ResponseEntity<UserDto> register(@RequestBody RestaurantOwnerSignupDto restaurantOwnerSignupDto) {
        return new ResponseEntity<>(restaurantOwnerService.register(restaurantOwnerSignupDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @SecurityRequirement(name = "restaurantOwnerBearerAuth")
    @PostMapping("/restaurants")
    @Operation(summary = "Register a restaurant by a restaurant owner")
    ResponseEntity<RestaurantDto> registerRestaurant(@RequestBody RestaurantDto restaurantDto) {
        return new ResponseEntity<>(restaurantService.createNewRestaurant(restaurantDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @SecurityRequirement(name = "restaurantOwnerBearerAuth")
    @GetMapping("/restaurants")
    @Operation(summary = "Retrieve list of all restaurants of a restaurant owner")
    ResponseEntity<List<RestaurantDto>> getAllRestaurantsOfAnOwner() {
        RestaurantOwner currentRestaurantOwner = restaurantOwnerService.getCurrentRestaurantOwner();
        return new ResponseEntity<>(restaurantService.getAllRestaurantsOfAnOwner(currentRestaurantOwner), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @SecurityRequirement(name = "restaurantOwnerBearerAuth")
    @GetMapping("/restaurants/{restaurantId}")
    @Operation(summary = "Retrieve a particular restaurant(by id) of a restaurant owner")
    ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable Long restaurantId) {
        return new ResponseEntity<>(restaurantService.getRestaurantById(restaurantId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @SecurityRequirement(name = "restaurantOwnerBearerAuth")
    @GetMapping("/restaurants/search")
    @Operation(summary = "Retrieve a particular restaurant (by name) of a restaurant owner")
    ResponseEntity<RestaurantDto> getRestaurantByName(@RequestParam String restaurantName) {
        return new ResponseEntity<>(restaurantService.getRestaurantByName(restaurantName), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @SecurityRequirement(name = "restaurantOwnerBearerAuth")
    @PutMapping("/restaurants/{restaurantId}")
    @Operation(summary = "Update details of a particular restaurant")
    ResponseEntity<RestaurantDto> updateRestaurantDetails(@PathVariable Long restaurantId, @RequestBody RestaurantDto restaurantDto) {
        return new ResponseEntity<>(restaurantService.updateRestaurantDetails(restaurantId, restaurantDto), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @SecurityRequirement(name = "restaurantOwnerBearerAuth")
    @PutMapping("/restaurants/{restaurantId}/updateAvailability")
    @Operation(summary = "Update availability of a particular restaurant")
    ResponseEntity<RestaurantDto> updateAvailabilityOfARestaurant(@PathVariable Long restaurantId) {
        return new ResponseEntity<>(restaurantService.updateAvailabilityOfAMenuItemInAMenu(restaurantId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @SecurityRequirement(name = "restaurantOwnerBearerAuth")
    @PutMapping("/restaurants/{restaurantId}/updateActiveState")
    @Operation(summary = "Update active state of a particular restaurant")
    ResponseEntity<RestaurantDto> updateActiveStateOfARestaurant(@PathVariable Long restaurantId) {
        return new ResponseEntity<>(restaurantService.updateActiveStateOfAMenuItemInAMenu(restaurantId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @SecurityRequirement(name = "restaurantOwnerBearerAuth")
    @PostMapping("/restaurants/{restaurantId}/menu")
    @Operation(summary = "Create menu of a particular restaurant")
    ResponseEntity<MenuDto> createMenuForARestaurant(@PathVariable Long restaurantId, @RequestBody MenuRequestDto menuRequestDto) {
        return new ResponseEntity<>(menuService.createMenuForARestaurant(restaurantId, menuRequestDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @SecurityRequirement(name = "restaurantOwnerBearerAuth")
    @GetMapping("/restaurants/{restaurantId}/menu")
    @Operation(summary = "Retrieve all menus of a particular restaurant")
    ResponseEntity<List<MenuDto>> getMenuForARestaurant(@PathVariable Long restaurantId) {
        return new ResponseEntity<>(menuService.getMenuForARestaurant(restaurantId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @SecurityRequirement(name = "restaurantOwnerBearerAuth")
    @GetMapping("/restaurants/{restaurantId}/menu/{menuId}")
    @Operation(summary = "Retrieve a particular menu of a particular restaurant")
    ResponseEntity<MenuDto> getMenuForARestaurantById(@PathVariable Long restaurantId, @PathVariable Long menuId) {
        return new ResponseEntity<>(menuService.getMenuForARestaurantById(restaurantId, menuId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @SecurityRequirement(name = "restaurantOwnerBearerAuth")
    @PutMapping("/restaurants/{restaurantId}/menu/{menuId}")
    @Operation(summary = "Add menu item in a menu of particular restaurant")
    ResponseEntity<MenuDto> addMenuItemInAMenu(@PathVariable Long restaurantId, @PathVariable Long menuId, @RequestBody MenuItemDto menuItem) {
        return new ResponseEntity<>(menuService.addMenuItemInAMenu(restaurantId, menuId, menuItem), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @SecurityRequirement(name = "restaurantOwnerBearerAuth")
    @GetMapping("/restaurants/{restaurantId}/menu/{menuId}/menuItem/{menuItemId}")
    @Operation(summary = "Retrieve a particular menu item of a particular restaurant")
    ResponseEntity<MenuItemDto> getMenuItemInAMenu(@PathVariable Long restaurantId, @PathVariable Long menuId, @PathVariable Long menuItemId) {
        return new ResponseEntity<>(menuService.getMenuItemInAMenu(restaurantId, menuId, menuItemId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @SecurityRequirement(name = "restaurantOwnerBearerAuth")
    @PutMapping("/restaurants/{restaurantId}/menu/{menuId}/menuItem/{menuItemId}")
    @Operation(summary = "Update details of particular menu item of a particular restaurant")
    ResponseEntity<MenuItemDto> updateAMenuItemInAMenu(@PathVariable Long restaurantId, @PathVariable Long menuId, @PathVariable Long menuItemId, @RequestBody MenuItemDto menuItem) {
        return new ResponseEntity<>(menuService.updateAMenuItemInAMenu(restaurantId, menuId, menuItemId, menuItem), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @SecurityRequirement(name = "restaurantOwnerBearerAuth")
    @PutMapping("/restaurants/{restaurantId}/menu/{menuId}/menuItem/{menuItemId}/updateAvailability")
    @Operation(summary = "Update availability of a particular menu item of a particular restaurant")
    ResponseEntity<MenuItemDto> updateAvailabilityOfAMenuItemInAMenu(@PathVariable Long restaurantId, @PathVariable Long menuId, @PathVariable Long menuItemId) {
        return new ResponseEntity<>(menuService.updateAvailabilityOfAMenuItemInAMenu(restaurantId, menuId, menuItemId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @SecurityRequirement(name = "restaurantOwnerBearerAuth")
    @PutMapping("/restaurants/{restaurantId}/menu/{menuId}/menuItem/{menuItemId}/updateActiveState")
    @Operation(summary = "Update active state of a particular menu item of a particular restaurant")
    ResponseEntity<MenuItemDto> updateActiveStateOfAMenuItemInAMenu(@PathVariable Long restaurantId, @PathVariable Long menuId, @PathVariable Long menuItemId) {
        return new ResponseEntity<>(menuService.updateActiveStateOfAMenuItemInAMenu(restaurantId, menuId, menuItemId), HttpStatus.OK);
    }


    @Secured("ROLE_RESTAURANT_OWNER")
    @SecurityRequirement(name = "restaurantOwnerBearerAuth")
    @GetMapping("/restaurants/{restaurantId}/orders")
    @Operation(summary = "Retrieve all the orders of a particular restaurant")
    ResponseEntity<List<OrderDto>> getAllOrdersOfARestaurant(@PathVariable Long restaurantId) {
        return new ResponseEntity<>(orderService.getAllOrdersOfARestaurant(restaurantId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @SecurityRequirement(name = "restaurantOwnerBearerAuth")
    @GetMapping("/restaurants/{restaurantId}/orders/{orderId}")
    @Operation(summary = "Retrieve a particular order of a particular restaurant")
    ResponseEntity<OrderDto> getOrderOfARestaurantByOrderId(@PathVariable Long restaurantId, @PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.getOrderOfARestaurantByOrderId(restaurantId, orderId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @SecurityRequirement(name = "restaurantOwnerBearerAuth")
    @PutMapping("/restaurants/{restaurantId}/orders/{orderId}/accept")
    @Operation(summary = "Accept order by a restaurant")
    ResponseEntity<OrderDto> acceptOrder(@PathVariable Long orderId, @PathVariable Long restaurantId) {
        return new ResponseEntity<>(orderService.acceptOrder(orderId, restaurantId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @SecurityRequirement(name = "restaurantOwnerBearerAuth")
    @Operation(summary = "Update status of an order to In Progress by a restaurant")
    @PutMapping("/restaurants/{restaurantId}/orders/{orderId}/updateOrderStatusToInProgress")
    ResponseEntity<OrderDto> updateStatusToInProgress(@PathVariable Long orderId, @PathVariable Long restaurantId) {
        return new ResponseEntity<>(orderService.updateStatusToInProgress(orderId, restaurantId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @SecurityRequirement(name = "restaurantOwnerBearerAuth")
    @PutMapping("/restaurants/{restaurantId}/orders/{orderId}/updateOrderStatusToOrderReady")
    @Operation(summary = "Update status of an order to Order Ready by a restaurant")
    ResponseEntity<OrderDto> updateStatusToOrderReady(@PathVariable Long orderId, @PathVariable Long restaurantId) {
        return new ResponseEntity<>(orderService.updateStatusToOrderReady(orderId, restaurantId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @SecurityRequirement(name = "restaurantOwnerBearerAuth")
    @PutMapping("/restaurants/{restaurantId}/orders/{orderId}/updateOrderStatusToReceivedByDeliveryPersonnel")
    @Operation(summary = "Update status of an order to Received By Delivery Personnel by a restaurant")
    ResponseEntity<OrderDto> updateStatusToReceivedByDeliveryPersonnel(@PathVariable Long orderId, @PathVariable Long restaurantId) {
        return new ResponseEntity<>(orderService.updateStatusToReceivedByDeliveryPersonnel(orderId, restaurantId), HttpStatus.OK);
    }
}
