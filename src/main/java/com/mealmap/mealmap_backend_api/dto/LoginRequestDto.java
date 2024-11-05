package com.mealmap.mealmap_backend_api.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
