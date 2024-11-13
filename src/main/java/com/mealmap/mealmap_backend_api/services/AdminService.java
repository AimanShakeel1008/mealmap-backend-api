package com.mealmap.mealmap_backend_api.services;

import com.mealmap.mealmap_backend_api.dto.AdminSignupDto;
import com.mealmap.mealmap_backend_api.dto.UserDto;
import com.mealmap.mealmap_backend_api.entities.Admin;

public interface AdminService {
    UserDto deactivateUser(String userEmail);

    UserDto activateUser(String userEmail);

    Admin getCurrentAdmin();

    UserDto register(AdminSignupDto adminSignUpDto);
}
