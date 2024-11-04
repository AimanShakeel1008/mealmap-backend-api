package com.mealmap.mealmap_backend_api.services;

import com.mealmap.mealmap_backend_api.dto.DeliveryRequestDto;
import com.mealmap.mealmap_backend_api.dto.SignupDto;
import com.mealmap.mealmap_backend_api.dto.UserDto;
import com.mealmap.mealmap_backend_api.entities.DeliveryPersonnel;
import com.mealmap.mealmap_backend_api.entities.User;
import com.mealmap.mealmap_backend_api.entities.enums.DeliveryRequestStatus;

public interface DeliveryPersonnelService {
    UserDto register(SignupDto signupDto);

    DeliveryPersonnel createNewDeliveryPersonnel(User user);

    DeliveryPersonnel updateDeliveryPersonnelAvailability(DeliveryPersonnel deliveryPersonnel, boolean available);
}
