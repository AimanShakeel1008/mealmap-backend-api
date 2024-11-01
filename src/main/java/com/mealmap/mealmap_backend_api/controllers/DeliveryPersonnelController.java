package com.mealmap.mealmap_backend_api.controllers;

import com.mealmap.mealmap_backend_api.dto.SignupDto;
import com.mealmap.mealmap_backend_api.dto.UserDto;
import com.mealmap.mealmap_backend_api.services.DeliveryPersonnelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/deliveryPersonnel")
@RequiredArgsConstructor
public class DeliveryPersonnelController {

    private final DeliveryPersonnelService deliveryPersonnelService;

    @PostMapping("/register")
    ResponseEntity<UserDto> register(@RequestBody SignupDto signupDto) {
        return new ResponseEntity<>(deliveryPersonnelService.register(signupDto), HttpStatus.CREATED);
    }
}
