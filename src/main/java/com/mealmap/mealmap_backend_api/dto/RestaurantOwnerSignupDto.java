package com.mealmap.mealmap_backend_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RestaurantOwnerSignupDto extends SignupDto{

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Contact number must be valid and contain between 7 to 15 digits")
    private String contactNumber;

    public RestaurantOwnerSignupDto(String name, String email, String password, String contactNumber) {
        super(name, email, password);
        this.contactNumber = contactNumber;
    }
}
