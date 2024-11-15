package com.mealmap.mealmap_backend_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerSignupDto extends SignupDto {

    @NotBlank(message = "Address is required")
    @Size(max = 100, message = "Address must not exceed 100 characters")
    private String address;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Contact number must be valid and contain between 7 to 15 digits")
    private String contactNumber;

    public CustomerSignupDto(String name, String email, String password, String address, String contactNumber) {
        super(name, email, password);
        this.address = address;
        this.contactNumber = contactNumber;
    }
}
