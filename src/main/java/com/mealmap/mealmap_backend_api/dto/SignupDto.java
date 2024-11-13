package com.mealmap.mealmap_backend_api.dto;

import lombok.Data;

@Data
public class SignupDto {

    private String name;

    private String email;

    private String password;

    public SignupDto() {
    }

    public SignupDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
