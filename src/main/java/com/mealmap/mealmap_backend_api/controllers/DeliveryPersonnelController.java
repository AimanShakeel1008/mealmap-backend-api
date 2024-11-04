package com.mealmap.mealmap_backend_api.controllers;

import com.mealmap.mealmap_backend_api.dto.DeliveryDto;
import com.mealmap.mealmap_backend_api.dto.DeliveryRequestDto;
import com.mealmap.mealmap_backend_api.dto.SignupDto;
import com.mealmap.mealmap_backend_api.dto.UserDto;
import com.mealmap.mealmap_backend_api.entities.enums.DeliveryRequestStatus;
import com.mealmap.mealmap_backend_api.entities.enums.DeliveryStatus;
import com.mealmap.mealmap_backend_api.services.DeliveryPersonnelService;
import com.mealmap.mealmap_backend_api.services.DeliveryRequestService;
import com.mealmap.mealmap_backend_api.services.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<UserDto> register(@RequestBody SignupDto signupDto) {
        return new ResponseEntity<>(deliveryPersonnelService.register(signupDto), HttpStatus.CREATED);
    }

    @GetMapping("/deliveryRequests")
    ResponseEntity<List<DeliveryRequestDto>> getAllDeliveryRequests() {
        List<DeliveryRequestDto> deliveryRequestDtoList = deliveryRequestService.getAllDeliveryRequests();

        return new ResponseEntity<>(deliveryRequestDtoList, HttpStatus.OK);
    }

    @PutMapping("/deliveryRequests/{deliveryRequestId}")
    ResponseEntity<DeliveryDto> acceptDeliveryRequest(@PathVariable Long deliveryRequestId) {

        return new ResponseEntity<>(deliveryRequestService
                .acceptDeliveryRequest(deliveryRequestId), HttpStatus.OK);

    }

    @GetMapping("/deliveries")
    ResponseEntity<List<DeliveryDto>> getAllDeliveries() {
        List<DeliveryDto> deliveryDtoList = deliveryService.getAllDeliveries();

        return new ResponseEntity<>(deliveryDtoList, HttpStatus.OK);
    }

    @PutMapping("/deliveries/{deliveryId}")
    ResponseEntity<DeliveryDto> updateDeliveryStatus(@PathVariable Long deliveryId, @RequestParam DeliveryStatus deliveryStatus) {
        DeliveryDto deliveryDto = deliveryService.updateDeliveryStatus(deliveryId, deliveryStatus);

        return new ResponseEntity<>(deliveryDto, HttpStatus.OK);
    }
}
