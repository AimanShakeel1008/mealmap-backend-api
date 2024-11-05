package com.mealmap.mealmap_backend_api.dto;

import lombok.Data;

@Data
public class CustomerSignupDto extends SignupDto {

    private String address;

    private String contactNumber;
}
