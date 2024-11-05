package com.mealmap.mealmap_backend_api.utils;

import com.mealmap.mealmap_backend_api.dto.SignupDto;
import org.springframework.stereotype.Component;

@Component
public class SignupMapper {

    public static SignupDto extractUserSpecificDetails(SignupDto signupDto) {
        SignupDto userSpecificDetails = new SignupDto();

        userSpecificDetails.setName(signupDto.getName());
        userSpecificDetails.setEmail(signupDto.getEmail());
        userSpecificDetails.setPassword(signupDto.getPassword());

        return userSpecificDetails;
    }
}
