package com.mealmap.mealmap_backend_api.controllers;

import com.mealmap.mealmap_backend_api.dto.*;
import com.mealmap.mealmap_backend_api.entities.RestaurantOwner;
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
    ResponseEntity<List<RestaurantDto>> getAllRestaurantsOfAnOwner() {
        RestaurantOwner currentRestaurantOwner = restaurantOwnerService.getCurrentRestaurantOwner();
        return new ResponseEntity<>(restaurantService.getAllRestaurantsOfAnOwner(currentRestaurantOwner), HttpStatus.OK);
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
    @PutMapping("/restaurants/{restaurantId}")
    ResponseEntity<RestaurantDto> updateRestaurantDetails(@PathVariable Long restaurantId, @RequestBody RestaurantDto restaurantDto) {
        return new ResponseEntity<>(restaurantService.updateRestaurantDetails(restaurantId, restaurantDto), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @PutMapping("/restaurants/{restaurantId}/updateAvailability")
    ResponseEntity<RestaurantDto> updateAvailabilityOfARestaurant(@PathVariable Long restaurantId) {
        return new ResponseEntity<>(restaurantService.updateAvailabilityOfAMenuItemInAMenu(restaurantId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @PutMapping("/restaurants/{restaurantId}/updateActiveState")
    ResponseEntity<RestaurantDto> updateActiveStateOfARestaurant(@PathVariable Long restaurantId) {
        return new ResponseEntity<>(restaurantService.updateActiveStateOfAMenuItemInAMenu(restaurantId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @PostMapping("/restaurants/{restaurantId}/menu")
    ResponseEntity<MenuDto> createMenuForARestaurant(@PathVariable Long restaurantId, @RequestBody MenuRequestDto menuRequestDto) {
        return new ResponseEntity<>(menuService.createMenuForARestaurant(restaurantId, menuRequestDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @GetMapping("/restaurants/{restaurantId}/menu")
    ResponseEntity<List<MenuDto>> getMenuForARestaurant(@PathVariable Long restaurantId) {
        return new ResponseEntity<>(menuService.getMenuForARestaurant(restaurantId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @GetMapping("/restaurants/{restaurantId}/menu/{menuId}")
    ResponseEntity<MenuDto> getMenuForARestaurantById(@PathVariable Long restaurantId, @PathVariable Long menuId) {
        return new ResponseEntity<>(menuService.getMenuForARestaurantById(restaurantId, menuId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @PutMapping("/restaurants/{restaurantId}/menu/{menuId}")
    ResponseEntity<MenuDto> addMenuItemInAMenu(@PathVariable Long restaurantId, @PathVariable Long menuId, @RequestBody MenuItemDto menuItem) {
        return new ResponseEntity<>(menuService.addMenuItemInAMenu(restaurantId, menuId, menuItem), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @GetMapping("/restaurants/{restaurantId}/menu/{menuId}/menuItem/{menuItemId}")
    ResponseEntity<MenuItemDto> getMenuItemInAMenu(@PathVariable Long restaurantId, @PathVariable Long menuId, @PathVariable Long menuItemId) {
        return new ResponseEntity<>(menuService.getMenuItemInAMenu(restaurantId, menuId, menuItemId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @PutMapping("/restaurants/{restaurantId}/menu/{menuId}/menuItem/{menuItemId}")
    ResponseEntity<MenuItemDto> updateAMenuItemInAMenu(@PathVariable Long restaurantId, @PathVariable Long menuId, @PathVariable Long menuItemId, @RequestBody MenuItemDto menuItem) {
        return new ResponseEntity<>(menuService.updateAMenuItemInAMenu(restaurantId, menuId, menuItemId, menuItem), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @PutMapping("/restaurants/{restaurantId}/menu/{menuId}/menuItem/{menuItemId}/updateAvailability")
    ResponseEntity<MenuItemDto> updateAvailabilityOfAMenuItemInAMenu(@PathVariable Long restaurantId, @PathVariable Long menuId, @PathVariable Long menuItemId) {
        return new ResponseEntity<>(menuService.updateAvailabilityOfAMenuItemInAMenu(restaurantId, menuId, menuItemId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @PutMapping("/restaurants/{restaurantId}/menu/{menuId}/menuItem/{menuItemId}/updateActiveState")
    ResponseEntity<MenuItemDto> updateActiveStateOfAMenuItemInAMenu(@PathVariable Long restaurantId, @PathVariable Long menuId, @PathVariable Long menuItemId) {
        return new ResponseEntity<>(menuService.updateActiveStateOfAMenuItemInAMenu(restaurantId, menuId, menuItemId), HttpStatus.OK);
    }


    @Secured("ROLE_RESTAURANT_OWNER")
    @GetMapping("/restaurants/{restaurantId}/orders")
    ResponseEntity<List<OrderDto>> getAllOrdersOfARestaurant(@PathVariable Long restaurantId) {
        return new ResponseEntity<>(orderService.getAllOrdersOfARestaurant(restaurantId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @GetMapping("/restaurants/{restaurantId}/orders/{orderId}")
    ResponseEntity<OrderDto> getOrderOfARestaurantByOrderId(@PathVariable Long restaurantId, @PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.getOrderOfARestaurantByOrderId(restaurantId, orderId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @PutMapping("/restaurants/{restaurantId}/orders/{orderId}/accept")
    ResponseEntity<OrderDto> acceptOrder(@PathVariable Long orderId, @PathVariable Long restaurantId) {
        return new ResponseEntity<>(orderService.acceptOrder(orderId, restaurantId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @PutMapping("/restaurants/{restaurantId}/orders/{orderId}/updateOrderStatusToInProgress")
    ResponseEntity<OrderDto> updateStatusToInProgress(@PathVariable Long orderId, @PathVariable Long restaurantId) {
        return new ResponseEntity<>(orderService.updateStatusToInProgress(orderId, restaurantId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @PutMapping("/restaurants/{restaurantId}/orders/{orderId}/updateOrderStatusToOrderReady")
    ResponseEntity<OrderDto> updateStatusToOrderReady(@PathVariable Long orderId, @PathVariable Long restaurantId) {
        return new ResponseEntity<>(orderService.updateStatusToOrderReady(orderId, restaurantId), HttpStatus.OK);
    }

    @Secured("ROLE_RESTAURANT_OWNER")
    @PutMapping("/restaurants/{restaurantId}/orders/{orderId}/updateOrderStatusToReceivedByDeliveryPersonnel")
    ResponseEntity<OrderDto> updateStatusToReceivedByDeliveryPersonnel(@PathVariable Long orderId, @PathVariable Long restaurantId) {
        return new ResponseEntity<>(orderService.updateStatusToReceivedByDeliveryPersonnel(orderId, restaurantId), HttpStatus.OK);
    }
}
