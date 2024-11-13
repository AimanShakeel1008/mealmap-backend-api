package com.mealmap.mealmap_backend_api.dto;

import lombok.Data;

@Data
public class CustomerSignupDto extends SignupDto {

    private String address;

    private String contactNumber;

    public CustomerSignupDto(String name, String email, String password, String address, String contactNumber) {
        super(name, email, password);
        this.address = address;
        this.contactNumber = contactNumber;
    }
}
