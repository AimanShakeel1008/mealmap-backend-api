package com.mealmap.mealmap_backend_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantOwnerDto {


    private Long id;
    private UserDto user;
    private String contactNumber;
    private List<RestaurantDto> restaurants;
}
