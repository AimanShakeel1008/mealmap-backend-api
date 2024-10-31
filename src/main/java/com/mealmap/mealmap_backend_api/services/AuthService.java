package com.mealmap.mealmap_backend_api.services;

import com.mealmap.mealmap_backend_api.dto.SignupDto;
import com.mealmap.mealmap_backend_api.entities.User;
import com.mealmap.mealmap_backend_api.entities.enums.Role;

import java.util.Set;

public interface AuthService {
    User signup(SignupDto signupDto, Set<Role> roles);
}
