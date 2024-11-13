package com.mealmap.mealmap_backend_api.dto;

import lombok.Data;

@Data
public class RestaurantOwnerSignupDto extends SignupDto{

    private String contactNumber;

    public RestaurantOwnerSignupDto(String name, String email, String password, String contactNumber) {
        super(name, email, password);
        this.contactNumber = contactNumber;
    }
}
