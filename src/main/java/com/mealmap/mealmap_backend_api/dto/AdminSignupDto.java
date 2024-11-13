package com.mealmap.mealmap_backend_api.dto;

import lombok.Data;

@Data
public class AdminSignupDto extends SignupDto{
    private String contactNumber;

    public AdminSignupDto(String name, String email, String password, String contactNumber) {
        super(name, email, password);
        this.contactNumber = contactNumber;
    }

    public AdminSignupDto() {
    }
}
