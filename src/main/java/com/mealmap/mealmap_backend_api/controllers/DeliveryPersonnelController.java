package com.mealmap.mealmap_backend_api.controllers;

import com.mealmap.mealmap_backend_api.dto.DeliveryDto;
import com.mealmap.mealmap_backend_api.dto.DeliveryPersonnelSignupDto;
import com.mealmap.mealmap_backend_api.dto.DeliveryRequestDto;
import com.mealmap.mealmap_backend_api.dto.UserDto;
import com.mealmap.mealmap_backend_api.services.DeliveryPersonnelService;
import com.mealmap.mealmap_backend_api.services.DeliveryRequestService;
import com.mealmap.mealmap_backend_api.services.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @Operation(summary = "Register a Delivery Personnel")
    ResponseEntity<UserDto> register(@RequestBody DeliveryPersonnelSignupDto deliveryPersonnelSignupDto) {
        return new ResponseEntity<>(deliveryPersonnelService.register(deliveryPersonnelSignupDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_DELIVERY_PERSONNEL")
    @SecurityRequirement(name = "deliveryPersonnelBearerAuth")
    @GetMapping("/deliveryRequests")
    @Operation(summary = "Retrieve all pending delivery requests")
    ResponseEntity<List<DeliveryRequestDto>> getAllDeliveryRequests() {
        List<DeliveryRequestDto> deliveryRequestDtoList = deliveryRequestService.getAllDeliveryRequests();

        return new ResponseEntity<>(deliveryRequestDtoList, HttpStatus.OK);
    }

    @Secured("ROLE_DELIVERY_PERSONNEL")
    @SecurityRequirement(name = "deliveryPersonnelBearerAuth")
    @GetMapping("/deliveryRequests/{deliveryRequestId}")
    @Operation(summary = "Retrieve a particular delivery request")
    ResponseEntity<DeliveryRequestDto> getDeliveryRequestById(@PathVariable Long deliveryRequestId) {
        DeliveryRequestDto deliveryRequestDtoList = deliveryRequestService.getDeliveryRequestById(deliveryRequestId);

        return new ResponseEntity<>(deliveryRequestDtoList, HttpStatus.OK);
    }

    @Secured("ROLE_DELIVERY_PERSONNEL")
    @SecurityRequirement(name = "deliveryPersonnelBearerAuth")
    @PutMapping("/deliveryRequests/{deliveryRequestId}")
    @Operation(summary = "Accept a delivery request")
    ResponseEntity<DeliveryDto> acceptDeliveryRequest(@PathVariable Long deliveryRequestId) {

        return new ResponseEntity<>(deliveryRequestService
                .acceptDeliveryRequest(deliveryRequestId), HttpStatus.OK);

    }

    @Secured("ROLE_DELIVERY_PERSONNEL")
    @SecurityRequirement(name = "deliveryPersonnelBearerAuth")
    @GetMapping("/deliveries")
    @Operation(summary = "Retrieve all deliveries of a delivery personnel")
    ResponseEntity<List<DeliveryDto>> getAllDeliveriesOfADeliveryPersonnel() {
        List<DeliveryDto> deliveryDtoList = deliveryService.getAllDeliveriesOfADeliveryPersonnel();

        return new ResponseEntity<>(deliveryDtoList, HttpStatus.OK);
    }

    @Secured("ROLE_DELIVERY_PERSONNEL")
    @SecurityRequirement(name = "deliveryPersonnelBearerAuth")
    @GetMapping("/deliveries/{deliveryId}")
    @Operation(summary = "Retrieve a particular of a delivery personnel")
    ResponseEntity<DeliveryDto> getDeliveryByDeliveryId(@PathVariable Long deliveryId) {
        DeliveryDto deliveryDto = deliveryService.getDeliveryByDeliveryId(deliveryId);

        return new ResponseEntity<>(deliveryDto, HttpStatus.OK);
    }

    @Secured("ROLE_DELIVERY_PERSONNEL")
    @SecurityRequirement(name = "deliveryPersonnelBearerAuth")
    @PutMapping("/deliveries/{deliveryId}/updateDeliveryStatusToOnTheWayToRestaurant")
    @Operation(summary = "Update status of  a particular of a delivery personnel to On The Way To Restaurant")
    ResponseEntity<DeliveryDto> updateDeliveryStatusToTheWayToRestaurant(@PathVariable Long deliveryId) {
        DeliveryDto deliveryDto = deliveryService.updateDeliveryStatusToTheWayToRestaurant(deliveryId);

        return new ResponseEntity<>(deliveryDto, HttpStatus.OK);
    }

    @Secured("ROLE_DELIVERY_PERSONNEL")
    @SecurityRequirement(name = "deliveryPersonnelBearerAuth")
    @PutMapping("/deliveries/{deliveryId}/updateDeliveryStatusToAtTheRestaurant")
    @Operation(summary = "Update status of  a particular of a delivery personnel to At the Restaurant")
    ResponseEntity<DeliveryDto> updateDeliveryStatusToAtTheRestaurant(@PathVariable Long deliveryId) {
        DeliveryDto deliveryDto = deliveryService.updateDeliveryStatusToAtTheRestaurant(deliveryId);

        return new ResponseEntity<>(deliveryDto, HttpStatus.OK);
    }

    @Secured("ROLE_DELIVERY_PERSONNEL")
    @SecurityRequirement(name = "deliveryPersonnelBearerAuth")
    @PutMapping("/deliveries/{deliveryId}/updateDeliveryStatusToOrderPickedUp")
    @Operation(summary = "Update status of  a particular of a delivery personnel to Order Picked Up")
    ResponseEntity<DeliveryDto> updateDeliveryStatusToOrderPickedUp(@PathVariable Long deliveryId) {
        DeliveryDto deliveryDto = deliveryService.updateDeliveryStatusToOrderPickedUp(deliveryId);

        return new ResponseEntity<>(deliveryDto, HttpStatus.OK);
    }

    @Secured("ROLE_DELIVERY_PERSONNEL")
    @SecurityRequirement(name = "deliveryPersonnelBearerAuth")
    @PutMapping("/deliveries/{deliveryId}/updateDeliveryStatusToOnTheWay")
    @Operation(summary = "Update status of  a particular of a delivery personnel to On The Way")
    ResponseEntity<DeliveryDto> updateDeliveryStatusToOnTheWay(@PathVariable Long deliveryId) {
        DeliveryDto deliveryDto = deliveryService.updateDeliveryStatusToOnTheWay(deliveryId);

        return new ResponseEntity<>(deliveryDto, HttpStatus.OK);
    }

    @Secured("ROLE_DELIVERY_PERSONNEL")
    @SecurityRequirement(name = "deliveryPersonnelBearerAuth")
    @PutMapping("/deliveries/{deliveryId}/updateDeliveryStatusToReachedCustomerLocation")
    @Operation(summary = "Update status of  a particular of a delivery personnel to Reached Customer Location")
    ResponseEntity<DeliveryDto> updateDeliveryStatusToReachedCustomerLocation(@PathVariable Long deliveryId) {
        DeliveryDto deliveryDto = deliveryService.updateDeliveryStatusToReachedCustomerLocation(deliveryId);

        return new ResponseEntity<>(deliveryDto, HttpStatus.OK);
    }

    @Secured("ROLE_DELIVERY_PERSONNEL")
    @SecurityRequirement(name = "deliveryPersonnelBearerAuth")
    @PutMapping("/deliveries/{deliveryId}/updateDeliveryStatusToOrderDelivered")
    @Operation(summary = "Update status of  a particular of a delivery personnel to Order Delivered")
    ResponseEntity<DeliveryDto> updateDeliveryStatusToOrderDelivered(@PathVariable Long deliveryId) {
        DeliveryDto deliveryDto = deliveryService.updateDeliveryStatusToOrderDelivered(deliveryId);

        return new ResponseEntity<>(deliveryDto, HttpStatus.OK);
    }
}
