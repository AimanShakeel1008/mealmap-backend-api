package com.mealmap.mealmap_backend_api.controllers;

import com.mealmap.mealmap_backend_api.dto.DeliveryDto;
import com.mealmap.mealmap_backend_api.dto.DeliveryPersonnelSignupDto;
import com.mealmap.mealmap_backend_api.dto.DeliveryRequestDto;
import com.mealmap.mealmap_backend_api.dto.UserDto;
import com.mealmap.mealmap_backend_api.services.DeliveryPersonnelService;
import com.mealmap.mealmap_backend_api.services.DeliveryRequestService;
import com.mealmap.mealmap_backend_api.services.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deliveryPersonnel")
@RequiredArgsConstructor
public class DeliveryPersonnelController {

    private final DeliveryPersonnelService deliveryPersonnelService;
    private final DeliveryRequestService deliveryRequestService;
    private final DeliveryService deliveryService;

    @PostMapping("/register")
    ResponseEntity<UserDto> register(@RequestBody DeliveryPersonnelSignupDto deliveryPersonnelSignupDto) {
        return new ResponseEntity<>(deliveryPersonnelService.register(deliveryPersonnelSignupDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_DELIVERY_PERSONNEL")
    @GetMapping("/deliveryRequests")
    ResponseEntity<List<DeliveryRequestDto>> getAllDeliveryRequests() {
        List<DeliveryRequestDto> deliveryRequestDtoList = deliveryRequestService.getAllDeliveryRequests();

        return new ResponseEntity<>(deliveryRequestDtoList, HttpStatus.OK);
    }

    @Secured("ROLE_DELIVERY_PERSONNEL")
    @GetMapping("/deliveryRequests/{deliveryRequestId}")
    ResponseEntity<DeliveryRequestDto> getDeliveryRequestById(@PathVariable Long deliveryRequestId) {
        DeliveryRequestDto deliveryRequestDtoList = deliveryRequestService.getDeliveryRequestById(deliveryRequestId);

        return new ResponseEntity<>(deliveryRequestDtoList, HttpStatus.OK);
    }

    @Secured("ROLE_DELIVERY_PERSONNEL")
    @PutMapping("/deliveryRequests/{deliveryRequestId}")
    ResponseEntity<DeliveryDto> acceptDeliveryRequest(@PathVariable Long deliveryRequestId) {

        return new ResponseEntity<>(deliveryRequestService
                .acceptDeliveryRequest(deliveryRequestId), HttpStatus.OK);

    }

    @Secured("ROLE_DELIVERY_PERSONNEL")
    @GetMapping("/deliveries")
    ResponseEntity<List<DeliveryDto>> getAllDeliveriesOfADeliveryPersonnel() {
        List<DeliveryDto> deliveryDtoList = deliveryService.getAllDeliveriesOfADeliveryPersonnel();

        return new ResponseEntity<>(deliveryDtoList, HttpStatus.OK);
    }

    @Secured("ROLE_DELIVERY_PERSONNEL")
    @GetMapping("/deliveries/{deliveryId}")
    ResponseEntity<DeliveryDto> getDeliveryByDeliveryId(@PathVariable Long deliveryId) {
        DeliveryDto deliveryDto = deliveryService.getDeliveryByDeliveryId(deliveryId);

        return new ResponseEntity<>(deliveryDto, HttpStatus.OK);
    }

    @Secured("ROLE_DELIVERY_PERSONNEL")
    @PutMapping("/deliveries/{deliveryId}/updateDeliveryStatusToOnTheWayToRestaurant")
    ResponseEntity<DeliveryDto> updateDeliveryStatusToTheWayToRestaurant(@PathVariable Long deliveryId) {
        DeliveryDto deliveryDto = deliveryService.updateDeliveryStatusToTheWayToRestaurant(deliveryId);

        return new ResponseEntity<>(deliveryDto, HttpStatus.OK);
    }

    @Secured("ROLE_DELIVERY_PERSONNEL")
    @PutMapping("/deliveries/{deliveryId}/updateDeliveryStatusToAtTheRestaurant")
    ResponseEntity<DeliveryDto> updateDeliveryStatusToAtTheRestaurant(@PathVariable Long deliveryId) {
        DeliveryDto deliveryDto = deliveryService.updateDeliveryStatusToAtTheRestaurant(deliveryId);

        return new ResponseEntity<>(deliveryDto, HttpStatus.OK);
    }

    @Secured("ROLE_DELIVERY_PERSONNEL")
    @PutMapping("/deliveries/{deliveryId}/updateDeliveryStatusToOrderPickedUp")
    ResponseEntity<DeliveryDto> updateDeliveryStatusToOrderPickedUp(@PathVariable Long deliveryId) {
        DeliveryDto deliveryDto = deliveryService.updateDeliveryStatusToOrderPickedUp(deliveryId);

        return new ResponseEntity<>(deliveryDto, HttpStatus.OK);
    }

    @Secured("ROLE_DELIVERY_PERSONNEL")
    @PutMapping("/deliveries/{deliveryId}/updateDeliveryStatusToOnTheWay")
    ResponseEntity<DeliveryDto> updateDeliveryStatusToOnTheWay(@PathVariable Long deliveryId) {
        DeliveryDto deliveryDto = deliveryService.updateDeliveryStatusToOnTheWay(deliveryId);

        return new ResponseEntity<>(deliveryDto, HttpStatus.OK);
    }

    @Secured("ROLE_DELIVERY_PERSONNEL")
    @PutMapping("/deliveries/{deliveryId}/updateDeliveryStatusToReachedCustomerLocation")
    ResponseEntity<DeliveryDto> updateDeliveryStatusToReachedCustomerLocation(@PathVariable Long deliveryId) {
        DeliveryDto deliveryDto = deliveryService.updateDeliveryStatusToReachedCustomerLocation(deliveryId);

        return new ResponseEntity<>(deliveryDto, HttpStatus.OK);
    }

    @Secured("ROLE_DELIVERY_PERSONNEL")
    @PutMapping("/deliveries/{deliveryId}/updateDeliveryStatusToOrderDelivered")
    ResponseEntity<DeliveryDto> updateDeliveryStatusToOrderDelivered(@PathVariable Long deliveryId) {
        DeliveryDto deliveryDto = deliveryService.updateDeliveryStatusToOrderDelivered(deliveryId);

        return new ResponseEntity<>(deliveryDto, HttpStatus.OK);
    }
}
