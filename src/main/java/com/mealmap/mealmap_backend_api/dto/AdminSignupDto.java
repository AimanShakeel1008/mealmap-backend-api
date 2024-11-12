package com.mealmap.mealmap_backend_api.dto;

import lombok.Data;

@Data
public class AdminSignupDto extends SignupDto{
    private String contactNumber;
}
