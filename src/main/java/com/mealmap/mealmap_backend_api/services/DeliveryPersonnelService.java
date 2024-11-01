package com.mealmap.mealmap_backend_api.services;

import com.mealmap.mealmap_backend_api.dto.SignupDto;
import com.mealmap.mealmap_backend_api.dto.UserDto;
import com.mealmap.mealmap_backend_api.entities.Customer;
import com.mealmap.mealmap_backend_api.entities.DeliveryPersonnel;
import com.mealmap.mealmap_backend_api.entities.User;

public interface DeliveryPersonnelService {
    UserDto register(SignupDto signupDto);

    DeliveryPersonnel createNewDeliveryPersonnel(User user);
}
