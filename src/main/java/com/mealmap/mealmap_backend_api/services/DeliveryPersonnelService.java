package com.mealmap.mealmap_backend_api.services;

import com.mealmap.mealmap_backend_api.dto.DeliveryPersonnelSignupDto;
import com.mealmap.mealmap_backend_api.dto.DeliveryRequestDto;
import com.mealmap.mealmap_backend_api.dto.SignupDto;
import com.mealmap.mealmap_backend_api.dto.UserDto;
import com.mealmap.mealmap_backend_api.entities.Customer;
import com.mealmap.mealmap_backend_api.entities.DeliveryPersonnel;
import com.mealmap.mealmap_backend_api.entities.User;
import com.mealmap.mealmap_backend_api.entities.enums.DeliveryRequestStatus;
import com.mealmap.mealmap_backend_api.entities.enums.DeliveryStatus;

public interface DeliveryPersonnelService {
    UserDto register(DeliveryPersonnelSignupDto deliveryPersonnelSignupDto);

    DeliveryPersonnel getCurrentDeliveryPersonnel();

    DeliveryPersonnel createNewDeliveryPersonnel(DeliveryPersonnelSignupDto deliveryPersonnelSignupDto, User user);

    DeliveryPersonnel updateDeliveryPersonnelAvailability(DeliveryPersonnel deliveryPersonnel, boolean available);
}
